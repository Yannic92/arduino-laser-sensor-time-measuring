package de.klem.yannic.speedway;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class Controller {


    @FXML
    private TableView driverTable;

    @FXML
    private ComboBox<String> portSelector;

    @FXML
    private Button connectButton;

    private ConnectController connectController;

    @FXML
    private void initialize() {
        this.connectController = new ConnectController(portSelector, connectButton);
        refreshPorts();
        this.portSelector.getSelectionModel().selectFirst();
    }

    @FXML
    private void refreshPorts() {
        this.connectController.refreshPorts();
    }
}
