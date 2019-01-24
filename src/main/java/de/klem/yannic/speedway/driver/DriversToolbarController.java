package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.time.timetable.TimeTable;
import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DriversToolbarController implements SpeedwayController {

    @FXML
    private HBox root;

    @FXML
    private TextField driversFilter;

    public DriversToolbarController() {

    }

    @FXML
    private void openNewTimeTable() throws IOException {
        new TimeTable(root.getParent());
    }

    @FXML
    private void openNewDriverDialog() throws IOException {
        new NewDriverDialog(root.getParent());
    }

    @FXML
    private void exportDrivers() {

    }

    @FXML
    private void importDrivers() {

    }

    @FXML
    private void clearDrivers() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public TextField getDriversFilter() {
        return driversFilter;
    }
}
