package com.jamhour.educationhub.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum ControllerResource {
    LOGIN("src\\main\\resources\\com\\jamhour\\educationhub\\log-in.fxml", false),
    STUDENT_HOME("src\\main\\resources\\com\\jamhour\\educationhub\\student\\student-homeTab.fxml"),
    STUDENT_HOME_CENTER_PANE("src\\main\\resources\\com\\jamhour\\educationhub\\student\\student-home-centerBorderPane.fxml"),
    STUDENT_COURSES("src\\main\\resources\\com\\jamhour\\educationhub\\student\\courses.fxml"),
    STUDENT_EXAMS("src\\main\\resources\\com\\jamhour\\educationhub\\student\\examsTab.fxml"),
    COURSES_DIALOG("src\\main\\resources\\com\\jamhour\\educationhub\\student\\courseDialog.fxml"),
    ADMIN_HOME("src\\main\\resources\\com\\jamhour\\educationhub\\admin\\admin-home.fxml"),
    ADMIN_STUDENT_ACTIONS("src\\main\\resources\\com\\jamhour\\educationhub\\admin\\student-actions.fxml"),
    ADMIN_TEACHER_ACTIONS("src\\main\\resources\\com\\jamhour\\educationhub\\admin\\teacher-actions.fxml"),
    ADMIN_COURSE_ACTIONS("src\\main\\resources\\com\\jamhour\\educationhub\\admin\\course-actions.fxml"),
    ADMIN_ADD_STUDENT_DIALOG("src\\main\\resources\\com\\jamhour\\educationhub\\admin\\addStudent-dialog.fxml"),
    ADMIN_DELETE_STUDENT_DIALOG("src\\main\\resources\\com\\jamhour\\educationhub\\admin\\deleteStudent-dialog.fxml"),
    ADMIN_UPDATE_STUDENT_DIALOG("src\\main\\resources\\com\\jamhour\\educationhub\\admin\\updateStudent-dialog.fxml"),
    ADMIN_REGISTER_STUDENT_DIALOG("src\\main\\resources\\com\\jamhour\\educationhub\\admin\\registerStudent-dialog.fxml");


    private final FXMLLoader fxmlLoader = new FXMLLoader();

    @Getter
    private final Path path;
    @Getter
    private final boolean resizable;

    private Parent content;

    ControllerResource(String path, boolean resizable) {
        this.path = Paths.get(path);
        this.resizable = resizable;
    }

    ControllerResource(String path) {
        this(path, true);
    }

    @SneakyThrows(IOException.class)
    public Parent getContent() {
        if (content == null) {
            fxmlLoader.setLocation(path.toUri().toURL());
            content = fxmlLoader.load();
        }
        return content;
    }

    public <T> T getController() {
        return fxmlLoader.getController();
    }

}
