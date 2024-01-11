package com.jamhour.educationhub.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.nio.file.Path;

public interface Controller<T> {

    Path getControllerPath();

    @SuppressWarnings("unchecked")
    default T getController() {
        return (T) this;
    }

    default Parent loadContents() {
        try {
            return new FXMLLoader(getControllerPath().toUri().toURL()).load();
        } catch (IOException e) {
            System.err.println(STR."Failed to load \{getControllerPath()}");
            throw new RuntimeException(e);
        }
    }

    default boolean isResizable() {
        return true;
    }
}
