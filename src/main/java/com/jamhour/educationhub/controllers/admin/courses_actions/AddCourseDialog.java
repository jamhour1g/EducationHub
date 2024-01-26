package com.jamhour.educationhub.controllers.admin.courses_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import com.jamhour.data.Course;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class AddCourseDialog {


    @FXML
    private Message message;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField teacherIdTextField;

    @FXML
    public void initialize() {
        message.getStyleClass().add(Styles.ACCENT);
    }


    @FXML
    public static void showAddCourseDialog(Window owner) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Course");
        dialog.initOwner(owner);

        dialog.getDialogPane().setContent(ControllerResource.ADMIN_ADD_COURSE_DIALOG.getContent());
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                AddCourseDialog controller = ControllerResource.ADMIN_ADD_COURSE_DIALOG.getController();
                controller.addCourse();
            } else {
                dialog.close();
            }
        });
    }

    private void addCourse() {

        if (nameTextField.getText() == null || nameTextField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Invalid name.", "Please enter a valid name.");
            return;
        }

        if (idTextField.getText() == null || idTextField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Invalid ID.", "Please enter a valid ID.");
            return;
        }

        if (teacherIdTextField.getText() == null || teacherIdTextField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Invalid teacher ID.", "Please enter a valid teacher ID.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idTextField.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidSearchInput("Invalid ID.", "Please enter a valid ID.");
            return;
        }

        int teacherId;
        try {
            teacherId = Integer.parseInt(teacherIdTextField.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidSearchInput("Invalid experience.", "Please enter a valid experience.");
            return;
        }
        Course course = new Course(nameTextField.getText(), id, teacherId);

        Queries.insertIntoTable(Schema.Tables.COURSE, course);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course Added");
        alert.setHeaderText(null);
        alert.setContentText("Course added successfully.");
        alert.showAndWait();
    }


    private void showErrorOnInvalidSearchInput(String title, String description) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(description);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                AddCourseDialog.showAddCourseDialog(null);
            } else {
                alert.close();
            }
        });

    }

}
