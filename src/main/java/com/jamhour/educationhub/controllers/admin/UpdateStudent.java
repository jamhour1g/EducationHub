package com.jamhour.educationhub.controllers.admin;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UpdateStudent {

    @FXML
    private Message errorMessage;
    @FXML
    private Tile studentIdTile;

    @FXML
    private VBox studentInfoVBox;
    @FXML
    private Message studentInfoMessage;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button updateStudentButton;


    @FXML
    public void initialize() {
        errorMessage.getStyleClass().add(Styles.DANGER);
        studentInfoMessage.getStyleClass().add(Styles.ACCENT);
    }
}
