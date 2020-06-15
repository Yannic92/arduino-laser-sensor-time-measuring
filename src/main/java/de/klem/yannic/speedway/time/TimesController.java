package de.klem.yannic.speedway.time;

import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.arduino.event.ConnectivityEvent;
import de.klem.yannic.speedway.driver.Driver;
import de.klem.yannic.speedway.driver.Drivers;
import de.klem.yannic.speedway.driver.TimeTableController;
import de.klem.yannic.speedway.time.measure.LapTimer;
import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;


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
