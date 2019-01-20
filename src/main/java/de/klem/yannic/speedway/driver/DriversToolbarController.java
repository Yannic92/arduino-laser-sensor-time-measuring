package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.time.timetable.TimeTable;
import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DriversToolbarController implements SpeedwayController {

    @FXML
    private SplitPane topLevelSplitPane;

    @FXML
    private TextField driversFilter;

    public DriversToolbarController() {

    }

    @FXML
    private void openNewTimeTable() throws IOException {
        new TimeTable(topLevelSplitPane.getParent());
    }

    @FXML
    private void openNewDriverDialog() throws IOException {
        new NewDriverDialog(topLevelSplitPane.getParent());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        topLevelSplitPane.getStylesheets().add(".split-pane *.split-pane-divider {-fx-padding: 0 1 0 1;}");
    }

    public TextField getDriversFilter() {
        return driversFilter;
    }
}
