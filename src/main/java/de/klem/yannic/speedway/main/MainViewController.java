package de.klem.yannic.speedway.main;

import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.driver.NewDriverDialog;
import de.klem.yannic.speedway.main.drivers.DriversController;
import de.klem.yannic.speedway.main.toolbar.ArduinoToolbarController;
import de.klem.yannic.speedway.main.toolbar.DriversToolbarController;
import de.klem.yannic.speedway.timetable.TimeTable;
import de.klem.yannic.speedway.ui.SpeedwayController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainViewController implements SpeedwayController {

    @FXML
    private VBox mainViewRoot;



    @FXML
    private Button connectButton;

    @FXML
    private ToolBar arduinoToolbar;

    @FXML
    private SplitPane topLevelSplitPane;

    @FXML
    private DriversController driversController;

    private ArduinoToolbarController arduinoToolbarController;
    private DriversToolbarController driversToolbarController;

    public MainViewController() {
    }

    @FXML
    private void initialize() {
        this.arduinoToolbarController = new ArduinoToolbarController(new Arduino(), arduinoToolbar, connectButton);
        this.driversToolbarController = new DriversToolbarController();
        topLevelSplitPane.getStylesheets().add(".split-pane *.split-pane-divider {-fx-padding: 0 1 0 1;}");
    }

    @FXML
    private void openNewTimeTable() throws IOException {
        new TimeTable(mainViewRoot);
    }

    @FXML
    private void openNewDriverDialog() throws IOException {
        new NewDriverDialog(mainViewRoot, driversController.getDriversList());
    }

    @Override
    public void exit() {
        this.arduinoToolbarController.exit();
    }
}
