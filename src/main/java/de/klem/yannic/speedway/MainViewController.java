package de.klem.yannic.speedway;

import de.klem.yannic.speedway.driver.DriversController;
import de.klem.yannic.speedway.driver.DriversToolbarController;
import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements SpeedwayController {


    @FXML
    private DriversToolbarController driversToolbarController;

    @FXML
    private DriversController driversController;

    public MainViewController() {
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        driversController.setDriversFilter(driversToolbarController.getDriversFilter());
    }
}
