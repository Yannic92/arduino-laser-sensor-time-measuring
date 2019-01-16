package de.klem.yannic.speedway.overview;

import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.driver.NewDriverDialog;
import de.klem.yannic.speedway.overview.connect.ConnectController;
import de.klem.yannic.speedway.timetable.TimeTable;
import de.klem.yannic.speedway.ui.SpeedwayController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OverviewController implements SpeedwayController {


    private final List<TimeTable> timeTables = new ArrayList<>();

    @FXML
    private TableView driverTable;

    @FXML
    private Button connectButton;

    private ConnectController connectController;

    @FXML
    private void initialize() {
        this.connectController = new ConnectController(new Arduino(), connectButton);
    }

    @FXML
    private void openNewTimeTable() throws IOException {
        timeTables.add(new TimeTable());
    }

    @FXML
    private void openNewDriverDialog() {
        new NewDriverDialog();
    }

    @Override
    public void exit() {
        this.connectController.exit();
        for (final TimeTable timeTable : timeTables) {
            timeTable.hide();
        }
    }
}
