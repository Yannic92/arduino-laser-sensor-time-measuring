package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.arduino.event.ConnectivityEvent;
import de.klem.yannic.speedway.time.measure.LapTimer;
import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class DriversController implements SpeedwayController {

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
        Arduino.INSTANCE.addEventHandler(ConnectivityEvent.CONNECTED_TYPE, (event) -> updateStartTimeMeasuringButton());
        Arduino.INSTANCE.addEventHandler(ConnectivityEvent.CONNECTING_TYPE, (event) -> updateStartTimeMeasuringButton());
        Arduino.INSTANCE.addEventHandler(ConnectivityEvent.DISCONNECTED_TYPE, (event) -> updateStartTimeMeasuringButton());
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
    }

    private void updateStartTimeMeasuringButton() {
        if (this.selectedDriver != null && Arduino.INSTANCE.isConnected()) {
            this.startTimeMeasuringButton.setDisable(false);
        } else {
            this.startTimeMeasuringButton.setDisable(true);
        }
    }

    @FXML
    public void startTimeMeasuring() {
        this.driversTable.setDisable(true);
        this.startTimeMeasuringButton.setDisable(true);
        activeTimer = new LapTimer(selectedDriver, timeLabel, lapDurations -> {
            finishTimeMeasuring();
        });
        Arduino.INSTANCE.onLapTick(activeTimer);
        this.cancelTimeMeasuringButton.setDisable(false);
    }

    @FXML
    public void cancelTimeMeasuring() {
        activeTimer.stop();
    }

    private void finishTimeMeasuring() {
        this.cancelTimeMeasuringButton.setDisable(true);
        Arduino.INSTANCE.removeEventListener();
        updateStartTimeMeasuringButton();
        this.driversTable.setDisable(false);
    }
}
