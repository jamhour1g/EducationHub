package com.jamhour.educationhub.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum Controller {
    LOGIN("src\\main\\resources\\com\\jamhour\\educationhub\\log-in.fxml", false),
    STUDENT_HOME("src\\main\\resources\\com\\jamhour\\educationhub\\student\\student-homeTab.fxml", true),
    STUDENT_COURSES("src\\main\\resources\\com\\jamhour\\educationhub\\student\\courses.fxml", true),
    STUDENT_EXAMS("src\\main\\resources\\com\\jamhour\\educationhub\\student\\examsTab.fxml", true),
    STUDENT_HOME_CENTER_PANE("src\\main\\resources\\com\\jamhour\\educationhub\\student\\student-home-centerBorderPane.fxml", true),
    COURSES_DIALOG("src\\main\\resources\\com\\jamhour\\educationhub\\student\\courseDialog.fxml", true);

    private final FXMLLoader fxmlLoader = new FXMLLoader();

    @Getter
    private final Path path;
    @Getter
    private final boolean resizable;

    private Parent content;

    Controller(String path, boolean resizable) {
        this.path = Paths.get(path);
        this.resizable = resizable;
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
