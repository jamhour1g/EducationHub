package com.jamhour.educationhub.controllers.admin.student_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import com.jamhour.data.Student;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;

public class DeleteStudentDialog {

    @FXML
    private Message message;
    @FXML
    private TextField studentId;

    @FXML
    public void initialize() {
        message.getStyleClass().add(Styles.ACCENT);
    }

    public static void showDeleteStudentDialog(Window owner) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Student");
        dialog.setContentText("Enter the ID of the student you want to delete.");
        dialog.initOwner(owner);
        dialog.setDialogPane((DialogPane) ControllerResource.ADMIN_DELETE_STUDENT_DIALOG.getContent());

        dialog.showAndWait()
                .ifPresent(buttonType -> {
                    if (buttonType == ButtonType.OK) {
                        DeleteStudentDialog content = ControllerResource.ADMIN_DELETE_STUDENT_DIALOG.getController();
                        content.deleteStudent();
                    } else {
                        dialog.close();
                    }
                });
    }

    private void deleteStudent() {
        int parsedId;
        try {
            parsedId = Integer.parseInt(studentId.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidInput("Invalid ID.", "Please enter a valid ID.");
            return;
        }

        if (studentId.getText() == null || studentId.getText().isBlank()) {
            showErrorOnInvalidInput("Enter a value.", "Please enter the id you want to delete");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this student?");
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                Queries.deleteFromTableUsing(
                        Schema.Tables.STUDENT,
                        Student.Column.ID,
                        parsedId
                );

                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Success");
                alert2.setHeaderText(null);
                alert2.setContentText("Student deleted successfully!");
                alert2.showAndWait();
            } else {
                alert.close();
            }
        });

    }

    private void showErrorOnInvalidInput(String title, String description) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(description);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                showDeleteStudentDialog(null);
            } else {
                alert.close();
            }
        });
    }
}
