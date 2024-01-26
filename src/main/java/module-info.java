module com.jamhour.educationhub {

    requires static lombok;

    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires DatabaseConnector;
    requires java.sql;

    exports com.jamhour.educationhub;
    exports com.jamhour.educationhub.controllers;
    exports com.jamhour.educationhub.controllers.student;
    exports com.jamhour.educationhub.controllers.admin;
    exports com.jamhour.educationhub.controllers.admin.student_actions;
    exports com.jamhour.educationhub.controllers.admin.teacher_actions;


    opens com.jamhour.educationhub.controllers to javafx.fxml;
    opens com.jamhour.educationhub.controllers.student to javafx.fxml;
    opens com.jamhour.educationhub.controllers.admin to javafx.fxml;
    opens com.jamhour.educationhub.controllers.admin.student_actions to javafx.fxml;
    opens com.jamhour.educationhub.controllers.admin.teacher_actions to javafx.fxml;
}