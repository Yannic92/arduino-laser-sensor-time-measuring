package de.klem.yannic.speedway.main.drivers;

import de.klem.yannic.speedway.driver.Driver;
import de.klem.yannic.speedway.ui.SpeedwayController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


public class DriversController implements SpeedwayController, Initializable {

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

    private final ObservableList<Driver> driversList;

    public DriversController() {
        this.driversList = FXCollections.observableArrayList();
    }

    public ObservableList<Driver> getDriversList() {
        return driversList;
    }

    @Override
    public void exit() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        startNumber.setCellValueFactory(new PropertyValueFactory<>("startNumber"));
        driverClass.setCellValueFactory(new PropertyValueFactory<>("driverClass"));
        club.setCellValueFactory(new PropertyValueFactory<>("club"));

        driversTable.setItems(driversList);
    }
}
