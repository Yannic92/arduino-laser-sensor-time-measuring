package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.arduino.event.ConnectivityEvent;
import de.klem.yannic.speedway.time.measure.LapTimer;
import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class DriversController implements SpeedwayController {

    @FXML
    private HBox driversRoot;

    @FXML
    private TextField selectedDriverFirstName;

    @FXML
    private TextField selectedDriverLastName;

    @FXML
    private TextField selectedDriverStartNumber;

    @FXML
    private TextField selectedDriverClass;

    @FXML
    private TextField selectedDriverClub;

    @FXML
    private Button startTimeMeasuringButton;

    @FXML
    private Button cancelTimeMeasuringButton;

    @FXML
    private Button deleteDriverButton;

    @FXML
    private Label timeLabel;

    @FXML
    private TimeTableController timeTableController;

    private TextField driversFilter;

    private FilteredList<Driver> filteredDrivers;

    private Driver selectedDriver;

    private LapTimer activeTimer;


    public void setDriversFilter(final TextField driversFilter) {
        this.driversFilter = driversFilter;
        ObjectBinding<Predicate<? super Driver>> filterBinding =
                Bindings.createObjectBinding(() -> this::tableFilterPredicate, driversFilter.textProperty());
        this.filteredDrivers.predicateProperty().bind(filterBinding);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filteredDrivers = new FilteredList<>(Drivers.getInstance().getDriversList(), this::tableFilterPredicate);
        timeTableController.setDrivers(filteredDrivers);
        timeTableController.onDriverSelection((observable, oldValue, newValue) -> updateSelectedDriver(newValue));
        final Arduino arduino = Arduino.getSingletonInstance();
        arduino.addEventHandler(ConnectivityEvent.CONNECTED_TYPE, (event) -> updateStartTimeMeasuringButton());
        arduino.addEventHandler(ConnectivityEvent.CONNECTING_TYPE, (event) -> updateStartTimeMeasuringButton());
        arduino.addEventHandler(ConnectivityEvent.DISCONNECTED_TYPE, (event) -> updateStartTimeMeasuringButton());

        driversRoot.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.DELETE == event.getCode()) {
                this.deleteDriverButton.fire();
            }
        });
    }

    private boolean tableFilterPredicate(final Driver driver) {
        if (this.driversFilter == null) {
            return true;
        }

        return driver.anyFieldMatches(driversFilter.getText());
    }

    private void updateSelectedDriver(final Driver selectedDriver) {
        this.selectedDriver = selectedDriver;
        if (this.selectedDriver != null) {
            this.selectedDriverFirstName.setText(selectedDriver.getFirstName());
            this.selectedDriverLastName.setText(selectedDriver.getLastName());
            this.selectedDriverClub.setText(selectedDriver.getClub());
            this.selectedDriverClass.setText(selectedDriver.getDriverClass());
            this.selectedDriverStartNumber.setText(selectedDriver.getStartNumber());
        }
        updateStartTimeMeasuringButton();
        updateDeleteDriverButton();
    }

    private void updateStartTimeMeasuringButton() {
        if (this.selectedDriver != null && Arduino.getSingletonInstance().isConnected()) {
            this.startTimeMeasuringButton.setDisable(false);
        } else {
            this.startTimeMeasuringButton.setDisable(true);
        }
    }

    private void updateDeleteDriverButton() {
        this.deleteDriverButton.setDisable(this.selectedDriver == null);
    }

    @FXML
    public void deleteDriver() {
        deleteDriver(selectedDriver);
    }

    private void deleteDriver(final Driver driver) {
        Drivers.getInstance().remove(driver);
    }

    @FXML
    public void startTimeMeasuring() {
        this.deleteDriverButton.setDisable(true);
        Platform.runLater(() -> {
            timeLabel.setText("--:--:---");
        });
        this.timeTableController.disableSelection();
        this.startTimeMeasuringButton.setDisable(true);
        activeTimer = new LapTimer(selectedDriver, timeLabel, this::finishTimeMeasuring);
        Arduino.getSingletonInstance().onLapTick(activeTimer);
        this.cancelTimeMeasuringButton.setDisable(false);
    }

    @FXML
    public void cancelTimeMeasuring() {
        activeTimer.stop();
    }

    private void finishTimeMeasuring(final Duration[] lapDurations) {
        this.cancelTimeMeasuringButton.setDisable(true);
        Arduino.getSingletonInstance().removeLapTickHandler();
        updateDriver(selectedDriver, lapDurations);
        updateStartTimeMeasuringButton();
        this.timeTableController.enableSelection();
        this.deleteDriverButton.setDisable(false);
    }

    private void updateDriver(final Driver driver, Duration[] lapDurations) {
        deleteDriver(driver);
        final int challenges = 4;
        final int pylons = 2;
        final Run run = Run.newInstance()
                .setChallenges(challenges)
                .setPylons(pylons)
                .setLapTimes(Arrays.asList(lapDurations));

        if (!driver.getRun1().isPresent()) {
            Drivers.getInstance().add(driver.setRun1(run));
        } else if (!driver.getRun2().isPresent()) {
            Drivers.getInstance().add(driver.setRun2(run));
        } else {
            throw new IllegalStateException("Driver already has a record for both runs.");
        }

    }
}
