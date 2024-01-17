package com.jamhour.educationhub.controllers.student;

import atlantafx.base.util.Animations;
import com.jamhour.data.Student;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

import java.util.Random;

public class StudentHomeScreen {

    private static final ControllerResource controller = ControllerResource.STUDENT_HOME;

    @FXML
    private BorderPane contentBorderPane;

    @FXML
    private Button homeButton;
    @FXML
    private Button coursesButton;
    @FXML
    private Button examsButton;
    @Getter
    private Student student;

    public StudentHomeScreen() {
        int size = Queries.getAllInTable(Schema.Tables.STUDENT).size();
        int randStudentId = new Random().nextInt(1, size);

        Queries.<Student>getFromTableUsing(Schema.Tables.STUDENT, Student.Column.ID, randStudentId)
                .ifPresent(s -> this.student = s);

    }

    public static StudentHomeScreen getInstance() {
        return controller.getController();
    }

    public void initialize() {
        contentBorderPane.getChildren().add(ControllerResource.STUDENT_HOME_CENTER_PANE.getContent());
    }

    @FXML
    public void openExamsPage() {
        openToPage(ControllerResource.STUDENT_EXAMS, examsButton);
    }

    @FXML
    public void openCoursesPage() {
        openToPage(ControllerResource.STUDENT_COURSES, coursesButton);
    }

    @FXML
    public void openHomePage() {
        openToPage(ControllerResource.STUDENT_HOME_CENTER_PANE, homeButton);
    }

    private void openToPage(ControllerResource studentHomeCenterPane, Button homeButton) {
        contentBorderPane.getChildren().clear();
        contentBorderPane.getChildren().add(studentHomeCenterPane.getContent());
        Animations.pulse(homeButton).playFromStart();
    }

}
