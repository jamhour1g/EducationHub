package com.jamhour.educationhub.controllers;

import com.jamhour.educationhub.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LogInController implements Controller<LogInController> {
    private static final Path controllerPath = Paths.get("src\\main\\resources\\com\\jamhour\\educationhub\\log-in.fxml");
    private static final String TEACHER = "@teacher.com";
    private static final String STUDENT = "@student.com";
    private static final String ADMIN = "@admin.com";

    @FXML
    private TextField email;

    @Override
    public Path getControllerPath() {
        return controllerPath;
    }

    @FXML
    public void openInfoDialog() {
        String contentText = """
                There are three sorts of users: teacher, student, and administrator.
                Each has a domain: @student for students and @teacher for teachers.
                You can sign in with any email address or password as long as the domain is right.
                               
                You can log in as a teacher with this email and password: ahmad@teacher.com -> password : 1234.
                As a student: ahmad@student.com -> password: 1234.
                """;

        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("The primary goal of this page is to assist with the login procedure.");
        alert.setContentText(contentText);
        alert.initOwner(App.getInstance().getWindow());
        alert.show();
    }

    @FXML
    public void logIn() {
        String emailText = email.getText();
        if (emailText.isEmpty()) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Email must be filled");
            alert.setHeaderText("Please enter the email.");
            alert.setContentText("For help see the info icon at the bottom left corner of the window.");
            alert.initOwner(App.getInstance().getWindow());
            alert.show();

        } else if (!(emailText.endsWith(TEACHER) ||
                     emailText.endsWith(STUDENT) ||
                     emailText.endsWith(ADMIN))) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Email must be filled");
            alert.setHeaderText("Please enter a valid email.");
            alert.setContentText("For help see the info icon at the bottom left corner of the window.");
            alert.initOwner(App.getInstance().getWindow());
            alert.show();

        }

    }
}
