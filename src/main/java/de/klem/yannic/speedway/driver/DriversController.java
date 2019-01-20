package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.function.Predicate;


public class DriversController implements SpeedwayController {

    @FXML
    private TableView<Driver> driversTable;

    @FXML
    private TableColumn<Driver, String> firstName;

    @FXML
    private TableColumn<Driver, String> lastName;

    @FXML
    private TableColumn<Driver, String> startNumber;

    @FXML
    private TableColumn<Driver, String> driverClass;

    @FXML
    private TableColumn<Driver, String> club;

    private TextField driversFilter;

    private FilteredList<Driver> filteredDrivers;

    public void setDriversFilter(final TextField driversFilter) {
        this.driversFilter = driversFilter;
        ObjectBinding<Predicate<? super Driver>> filterBinding =
                Bindings.createObjectBinding(() -> this::tableFilterPredicate, driversFilter.textProperty());
        this.filteredDrivers.predicateProperty().bind(filterBinding);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        startNumber.setCellValueFactory(new PropertyValueFactory<>("startNumber"));
        driverClass.setCellValueFactory(new PropertyValueFactory<>("driverClass"));
        club.setCellValueFactory(new PropertyValueFactory<>("club"));

        filteredDrivers = new FilteredList<>(Drivers.getInstance().getDriversList(), this::tableFilterPredicate);
        driversTable.setItems(filteredDrivers);
    }

    private boolean tableFilterPredicate(final Driver driver) {
        if (this.driversFilter == null) {
            return true;
        }

        return driver.anyFieldMatches(driversFilter.getText());
    }
}
