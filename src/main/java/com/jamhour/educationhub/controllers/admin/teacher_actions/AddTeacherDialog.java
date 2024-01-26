package com.jamhour.educationhub.controllers.admin.teacher_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import com.jamhour.data.Teacher;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;

public class AddTeacherDialog {


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
    private TextField experienceTextField;
    @FXML
    private TextField salaryTextField;
    @FXML
    private TextField majorTextField;
    @FXML
    private DatePicker dateOfBirthDatePicker;

    @FXML
    public void initialize() {
        message.getStyleClass().add(Styles.ACCENT);
    }


    @FXML
    public static void showAddTeacherDialog(Window owner) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Teacher");
        dialog.initOwner(owner);

        dialog.getDialogPane().setContent(ControllerResource.ADMIN_ADD_TEACHER_DIALOG.getContent());
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                AddTeacherDialog controller = ControllerResource.ADMIN_ADD_TEACHER_DIALOG.getController();
                controller.addTeacher();
            } else {
                dialog.close();
            }
        });
    }

    private void addTeacher() {
        int id;
        try {
            id = Integer.parseInt(idTextField.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidSearchInput("Invalid ID.", "Please enter a valid ID.");
            return;
        }

        int experience;
        try {
            experience = Integer.parseInt(experienceTextField.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidSearchInput("Invalid experience.", "Please enter a valid experience.");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryTextField.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidSearchInput("Invalid salary.", "Please enter a valid salary.");
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

        if (experienceTextField.getText() == null || experienceTextField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Invalid experience.", "Please enter a valid experience.");
            return;
        }

        if (salaryTextField.getText() == null || salaryTextField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Invalid salary.", "Please enter a valid salary.");
            return;
        }

        if (majorTextField.getText() == null || majorTextField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Invalid major.", "Please enter a valid major.");
            return;
        }

        if (dateOfBirthDatePicker.getValue() == null) {
            showErrorOnInvalidSearchInput("Invalid date of birth.", "Please enter a valid date of birth.");
            return;
        }

        Teacher teacher = new Teacher(
                nameTextField.getText(),
                phoneTextField.getText(),
                emailTextField.getText(),
                majorTextField.getText(),
                salary,
                experience,
                dateOfBirthDatePicker.getValue(),
                id
        );

        Queries.insertIntoTable(Schema.Tables.TEACHER, teacher);
    }


    private void showErrorOnInvalidSearchInput(String title, String description) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(description);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                AddTeacherDialog.showAddTeacherDialog(null);
            } else {
                alert.close();
            }
        });

    }

}
