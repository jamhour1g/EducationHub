package com.jamhour.educationhub.controllers;

import com.jamhour.educationhub.App;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LogInController implements Controller<LogInController> {
    private static final Path controllerPath = Paths.get("src\\main\\resources\\com\\jamhour\\educationhub\\log-in.fxml");

    @FXML
    private ComboBox<String> logInAs;

    public void initialize() {
        logInAs.getItems().addAll("Student", "Teacher", "Admin");
        logInAs.getSelectionModel().selectFirst();
    }

    @Override
    public Path getControllerPath() {
        return controllerPath;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @FXML
    public void logIn() {
        switch (logInAs.getValue()) {
            case "Student" -> {
                Controller<StudentHomeScreen> studentHomeScreen = new StudentHomeScreen();
                App.getInstance().setSceneAndShow(studentHomeScreen.loadContents(), studentHomeScreen.isResizable());
            }
//            case "Teacher" -> App.getInstance().setSceneAndShow(new TeacherHomeScreen().loadContents());
//            case "Admin" -> App.getInstance().setSceneAndShow(new AdminHomeScreen().loadContents());
        }
    }
}
