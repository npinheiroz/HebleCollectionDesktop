module com.example.heblecollectiondesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.heblecollectiondesktop to javafx.fxml;
    exports com.example.heblecollectiondesktop;
}