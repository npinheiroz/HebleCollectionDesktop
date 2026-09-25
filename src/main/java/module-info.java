module com.example.heblecollectiondesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens com.example.heblecollectiondesktop.model to javafx.base, javafx.fxml;
    opens com.example.heblecollectiondesktop.controller to javafx.fxml;
    exports com.example.heblecollectiondesktop;
    exports com.example.heblecollectiondesktop.model;
    exports com.example.heblecollectiondesktop.controller;
}