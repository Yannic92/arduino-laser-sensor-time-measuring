package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.arduino.event.ConnectivityEvent;
import de.klem.yannic.speedway.time.measure.LapTimer;
import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class TimeTableController implements SpeedwayController {

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
    private TableColumn<Driver, String> run1Time;

    @FXML
    private TableColumn<Driver, String> run1Pylons;

    @FXML
    private TableColumn<Driver, String> run1Challenges;

    @FXML
    private TableColumn<Driver, String> run2Time;

    @FXML
    private TableColumn<Driver, String> run2Pylons;

    @FXML
    private TableColumn<Driver, String> run2Challenges;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        startNumber.setCellValueFactory(new PropertyValueFactory<>("startNumber"));
        driverClass.setCellValueFactory(new PropertyValueFactory<>("driverClass"));
        club.setCellValueFactory(new PropertyValueFactory<>("club"));
        run1Time.setCellValueFactory(driver -> {
            final Duration summedDuration = driver.getValue().getRun1().getSummedDuration();
            return new SimpleStringProperty(DurationFormatUtils.formatDuration(summedDuration.toMillis(), "mm:ss:SS"));
        });
        run1Challenges.setCellValueFactory(driver -> new SimpleStringProperty(String.valueOf(driver.getValue().getRun1().getChallenges())));
        run1Pylons.setCellValueFactory(driver -> new SimpleStringProperty(String.valueOf(driver.getValue().getRun1().getPylons())));
        run2Time.setCellValueFactory(driver -> {
            final Duration summedDuration = driver.getValue().getRun2().getSummedDuration();
            return new SimpleStringProperty(DurationFormatUtils.formatDuration(summedDuration.toMillis(), "mm:ss:SS"));
        });
        run2Challenges.setCellValueFactory(driver -> new SimpleStringProperty(String.valueOf(driver.getValue().getRun2().getChallenges())));
        run2Pylons.setCellValueFactory(driver -> new SimpleStringProperty(String.valueOf(driver.getValue().getRun2().getPylons())));
    }

    public void setDrivers(final FilteredList<Driver> drivers) {
        driversTable.setItems(drivers);
    }

    public void onDriverSelection(ChangeListener<? super Driver> changeListener) {
        driversTable.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    void disableSelection() {
        this.driversTable.setDisable(true);
    }

    void enableSelection() {
        this.driversTable.setDisable(false);
    }

}
