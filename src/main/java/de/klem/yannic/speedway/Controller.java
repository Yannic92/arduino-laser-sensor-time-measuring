package de.klem.yannic.speedway;

import de.klem.yannic.speedway.driver.NewDriverDialog;
import de.klem.yannic.speedway.timetable.TimeTable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class Controller {


    @FXML
    private TableView driverTable;

    @FXML
    private Button connectButton;

    private ConnectController connectController;

    @FXML
    private void initialize() {
        this.connectController = new ConnectController(connectButton);
    }

    @FXML
    private void openNewTimeTable() {
        new TimeTable();
    }

    @FXML
    private void openNewDriverDialog() {
        new NewDriverDialog();
    }
}
