module com.jamhour.educationhub {

    requires static lombok;

    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires DatabaseConnector;
    requires java.sql;

    exports com.jamhour.educationhub;
    exports com.jamhour.educationhub.controllers;

    opens com.jamhour.educationhub.controllers to javafx.fxml;
    exports com.jamhour.educationhub.controllers.student;
    opens com.jamhour.educationhub.controllers.student to javafx.fxml;
}