package com.jamhour.educationhub.controllers.admin;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import com.jamhour.data.Student;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class AddStudentDialog {


    @FXML
    private Message message;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;

    @FXML
    public void initialize() {
        message.getStyleClass().add(Styles.ACCENT);
    }


    @FXML
    public static void showAddStudentDialog(Window owner) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Student");
        dialog.initOwner(owner);

        dialog.getDialogPane().setContent(ControllerResource.ADMIN_ADD_STUDENT_DIALOG.getContent());
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                AddStudentDialog controller = ControllerResource.ADMIN_ADD_STUDENT_DIALOG.getController();
                controller.addStudent();
            } else {
                dialog.close();
            }
        });
    }

    private void addStudent() {
        int id;
        try {
            id = Integer.parseInt(idTextField.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidSearchInput("Invalid ID.", "Please enter a valid ID.");
            return;
        }

        if (nameTextField.getText() == null || nameTextField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Invalid name.", "Please enter a valid name.");
            return;
        }
        if (phoneTextField.getText() == null || phoneTextField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Invalid phone.", "Please enter a valid phone.");
            return;
        }
        if (emailTextField.getText() == null || emailTextField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Invalid email.", "Please enter a valid email.");
            return;
        }

        Student student = new Student(
                nameTextField.getText(),
                phoneTextField.getText(),
                emailTextField.getText(),
                id
        );
        Queries.insertIntoTable(Schema.Tables.STUDENT, student);
    }

    private void showErrorOnInvalidSearchInput(String title, String description) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(description);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                AddStudentDialog.showAddStudentDialog(null);
            } else {
                alert.close();
            }
        });

    }

}
