package com.jamhour.educationhub.controllers;

import com.jamhour.educationhub.App;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class LogInController {

    private static final ControllerResource controller = ControllerResource.LOGIN;

    @FXML
    private ComboBox<String> logInAs;

    public void initialize() {
        logInAs.getItems().addAll("Student", "Teacher", "Admin");
        logInAs.getSelectionModel().selectFirst();
    }

    @FXML
    public void logIn() {
        switch (logInAs.getValue()) {
            case "Student" ->
                    App.getInstance().setSceneAndShow(ControllerResource.STUDENT_HOME.getContent(), ControllerResource.STUDENT_HOME.isResizable());
//            case "Teacher" -> App.getInstance().setSceneAndShow(new TeacherHomeScreen().loadContents());
//            case "Admin" -> App.getInstance().setSceneAndShow(new AdminHomeScreen().loadContents());
        }
    }

    public static LogInController getInstance() {
        return controller.getController();
    }
}
