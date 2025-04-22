module nntc.antonov.coursework {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;


    opens nntc.volosov.coursework to javafx.fxml;
    exports nntc.volosov.coursework;
}