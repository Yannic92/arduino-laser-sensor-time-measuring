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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class DriversController implements SpeedwayController {

    @FXML
    private HBox driversRoot;

    @FXML
    private TableView<Driver> driversTable;

    @FXML
    private TableColumn<Driver, String> firstName;

    @FXML
    private TableColumn<Driver, String> lastName;

    @FXML
    private TableColumn<Driver, String> startNumber;

    @FXML
    private TableColumn<Driver, String> driverClass;

    @FXML
    private TableColumn<Driver, String> club;

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
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        startNumber.setCellValueFactory(new PropertyValueFactory<>("startNumber"));
        driverClass.setCellValueFactory(new PropertyValueFactory<>("driverClass"));
        club.setCellValueFactory(new PropertyValueFactory<>("club"));

        filteredDrivers = new FilteredList<>(Drivers.getInstance().getDriversList(), this::tableFilterPredicate);
        driversTable.setItems(filteredDrivers);
        driversTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> updateSelectedDriver(newValue));
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
        Drivers.getInstance().remove(selectedDriver);
    }

    @FXML
    public void startTimeMeasuring() {
        this.deleteDriverButton.setDisable(true);
        Platform.runLater(() -> {
            timeLabel.setText("--:--:---");
        });
        this.driversTable.setDisable(true);
        this.startTimeMeasuringButton.setDisable(true);
        activeTimer = new LapTimer(selectedDriver, timeLabel, lapDurations -> finishTimeMeasuring());
        Arduino.getSingletonInstance().onLapTick(activeTimer);
        this.cancelTimeMeasuringButton.setDisable(false);
    }

    @FXML
    public void cancelTimeMeasuring() {
        activeTimer.stop();
    }

    private void finishTimeMeasuring() {
        this.cancelTimeMeasuringButton.setDisable(true);
        Arduino.getSingletonInstance().removeLapTickHandler();
        updateStartTimeMeasuringButton();
        this.driversTable.setDisable(false);
        this.deleteDriverButton.setDisable(false);
    }
}
