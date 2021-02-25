package de.klem.yannic.speedway.time;

import de.klem.yannic.speedway.driver.Driver;
import de.klem.yannic.speedway.driver.Drivers;
import de.klem.yannic.speedway.driver.TimeTableController;
import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;


public class TimesController implements SpeedwayController {

    @FXML
    private TimeTableController timeTableController;

    private FilteredList<Driver> filteredDrivers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filteredDrivers = new FilteredList<>(Drivers.getInstance().getDriversList());
        timeTableController.setDrivers(filteredDrivers);
    }

}
