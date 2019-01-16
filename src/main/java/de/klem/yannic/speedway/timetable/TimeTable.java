package de.klem.yannic.speedway.timetable;

import de.klem.yannic.speedway.ui.SpeedwayStage;
import javafx.stage.Stage;

import java.io.IOException;

public class TimeTable extends SpeedwayStage {

    public TimeTable() throws IOException {
        super(new Stage(), "timetable.fxml", "Zeiten", "time.png");
        maximize();
//            String style = getClass().getClassLoader().getResource("style.css").toExternalForm();
//            stage.getScene().getStylesheets().add(style);

        stage.show();

    }

    @Override
    protected void exit() {
    }
}
