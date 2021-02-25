package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.time.timetable.TimeTable;
import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DriversToolbarController implements SpeedwayController {

    private static final String FILE_DESCRIPTION = "A file containing driver data separated by commas.";
    private static final String FILE_EXTENSION = "csv";
    private static final String DEFAULT_FILE_NAME = "drivers.csv";

    @FXML
    private HBox root;

    @FXML
    private TextField driversFilter;

    private final Drivers drivers;

    public DriversToolbarController() {
        this.drivers = Drivers.getInstance();
    }

    @FXML
    private void openNewTimeTable() throws IOException {
        new TimeTable(root.getParent());
    }

    @FXML
    private void openNewDriverDialog() throws IOException {
        new NewDriverDialog(root.getParent());
    }

    @FXML
    private void exportDrivers() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(new File("").getAbsolutePath()));
        fileChooser.setInitialFileName(DEFAULT_FILE_NAME);
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(FILE_DESCRIPTION, FILE_EXTENSION));
        fileChooser.setTitle("Fahrerliste exportieren");
        File file = fileChooser.showSaveDialog(root.getParent().getScene().getWindow());

        if (file != null) {
            Drivers.getInstance().save(file);
        }
    }

    @FXML
    private void importDrivers() {

        if (drivers.getDriversList().isEmpty()) {
            doImportDrivers();
            return;
        }

        Alert deleteConfirmationDialogue = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirmationDialogue.setTitle("Fahrerliste wirklich importieren?");
        deleteConfirmationDialogue.setHeaderText("Wollen Sie wirklich eine Fahrerliste importieren?");
        deleteConfirmationDialogue.setContentText("Die bestehende Fahrerliste und alle dazugehörigen gefahrenen " +
                "Zeiten gehen damit verloren.");
        deleteConfirmationDialogue.initOwner(root.getParent().getScene().getWindow());
        deleteConfirmationDialogue.initModality(Modality.WINDOW_MODAL);

        deleteConfirmationDialogue.showAndWait()
                .filter(ButtonType.OK::equals)
                .ifPresent((buttonType) -> this.doImportDrivers());
    }

    private void doImportDrivers() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(new File("").getAbsolutePath()));
        fileChooser.setInitialFileName(DEFAULT_FILE_NAME);
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(FILE_DESCRIPTION, FILE_EXTENSION));
        fileChooser.setTitle("Fahrerliste importieren");
        File file = fileChooser.showOpenDialog(root.getParent().getScene().getWindow());

        if (file != null) {
            Drivers.getInstance().load(file);
        }
    }

    @FXML
    private void clearDrivers() {
        Alert deleteConfirmationDialogue = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirmationDialogue.setTitle("Fahrerliste wirklich leeren?");
        deleteConfirmationDialogue.setHeaderText("Wollen Sie wirklich die Fahrerliste leeren?");
        deleteConfirmationDialogue.setContentText("Alle gefahrenen Zeiten gehen durch diesen Schritt verloren. " +
                "Um die Fahrer und Zeiten zu sichern, exportieren Sie die Liste zuerst.");
        deleteConfirmationDialogue.initOwner(root.getParent().getScene().getWindow());
        deleteConfirmationDialogue.initModality(Modality.WINDOW_MODAL);

        deleteConfirmationDialogue.showAndWait()
                .filter(ButtonType.OK::equals)
                .ifPresent((buttonType) -> this.drivers.clear());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public TextField getDriversFilter() {
        return driversFilter;
    }
}
