package com.jamhour.educationhub.controllers.admin;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DeleteStudentDialog {

    @FXML
    private Message message;
    @FXML
    private TextField studentId;

    @FXML
    public void initialize() {
        message.getStyleClass().add(Styles.ACCENT);
    }

}
