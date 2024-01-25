package com.jamhour.educationhub.controllers.admin;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class RegisterStudentDialog {

    @FXML
    private Message errorMessage;
    @FXML
    private Tile studentIdTile;
    @FXML
    private VBox coursesVBox;
    @FXML
    private Message studentInfoMessage;
    @FXML
    private TableView courseTableView;
    @FXML
    private TableColumn teacherNameColumn;
    @FXML
    private TableColumn checkBoxColumn;
    @FXML
    private TableColumn courseIdColumn;
    @FXML
    private TableColumn courseNameColumn;
    @FXML
    private Button registerStudentButton;

    @FXML
    public void initialize() {
        errorMessage.getStyleClass().add(Styles.DANGER);
        studentInfoMessage.getStyleClass().add(Styles.ACCENT);
    }


    public static void showRegisterStudentDialog(Window window) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Register Student");
        dialog.initOwner(window);
        dialog.setDialogPane((DialogPane) ControllerResource.ADMIN_REGISTER_STUDENT_DIALOG.getContent());
        dialog.showAndWait();

    }

}
