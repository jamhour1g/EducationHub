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
            case "Admin" ->
                    App.getInstance().setSceneAndShow(ControllerResource.ADMIN_HOME.getContent(), ControllerResource.ADMIN_HOME.isResizable());
        }
    }

    public static LogInController getInstance() {
        return controller.getController();
    }

}
