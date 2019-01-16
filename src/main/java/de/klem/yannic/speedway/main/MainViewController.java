package de.klem.yannic.speedway.main;

import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.driver.NewDriverDialog;
import de.klem.yannic.speedway.main.toolbar.ArduinoToolbarController;
import de.klem.yannic.speedway.main.toolbar.DriversToolbarController;
import de.klem.yannic.speedway.timetable.TimeTable;
import de.klem.yannic.speedway.ui.SpeedwayController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainViewController implements SpeedwayController {


    private final List<TimeTable> timeTables = new ArrayList<>();

    @FXML
    private TableView driverTable;

    @FXML
    private Button connectButton;

    @FXML
    private ToolBar arduinoToolbar;

    @FXML
    private SplitPane topLevelSplitPane;

    private ArduinoToolbarController arduinoToolbarController;
    private DriversToolbarController driversToolbarController;

    @FXML
    private void initialize() {
        this.arduinoToolbarController = new ArduinoToolbarController(new Arduino(), arduinoToolbar, connectButton);
        this.driversToolbarController = new DriversToolbarController();
        topLevelSplitPane.getStylesheets().add(".split-pane *.split-pane-divider {-fx-padding: 0 1 0 1;}");
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
        this.arduinoToolbarController.exit();
        for (final TimeTable timeTable : timeTables) {
            timeTable.hide();
        }
    }
}
