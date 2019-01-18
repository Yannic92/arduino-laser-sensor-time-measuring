package de.klem.yannic.speedway.main.toolbar;

import de.klem.yannic.speedway.driver.NewDriverDialog;
import de.klem.yannic.speedway.main.drivers.DriversController;
import de.klem.yannic.speedway.timetable.TimeTable;
import de.klem.yannic.speedway.ui.SpeedwayController;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DriversToolbarController implements SpeedwayController {

    @FXML
    private SplitPane topLevelSplitPane;

    public DriversToolbarController() {

    }

    @FXML
    private void openNewTimeTable() throws IOException {
        new TimeTable(topLevelSplitPane.getParent());
    }

    @FXML
    private void openNewDriverDialog() throws IOException {
        new NewDriverDialog(topLevelSplitPane.getParent(), DriversController.DRIVERS);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        topLevelSplitPane.getStylesheets().add(".split-pane *.split-pane-divider {-fx-padding: 0 1 0 1;}");
    }
}
