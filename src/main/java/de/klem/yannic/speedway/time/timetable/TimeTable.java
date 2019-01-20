package de.klem.yannic.speedway.time.timetable;

import de.klem.yannic.speedway.utils.ui.SpeedwayStage;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class TimeTable extends SpeedwayStage {

    public TimeTable(final Parent parent) throws IOException {
        super(new Stage(), "views/time/timetable.fxml", "Zeiten", "icons/time.png");
        maximize();
//            String style = getClass().getClassLoader().getResource("style.css").toExternalForm();
//            stage.getScene().getStylesheets().add(style);
        stage.initOwner(parent.getScene().getWindow());
        stage.show();

    }

    @Override
    protected void exit() {
    }
}
