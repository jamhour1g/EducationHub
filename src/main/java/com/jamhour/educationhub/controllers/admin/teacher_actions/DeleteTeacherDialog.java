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

public class DeleteTeacherDialog {

    @FXML
    private Message message;
    @FXML
    private TextField teacherId;

    @FXML
    public void initialize() {
        message.getStyleClass().add(Styles.ACCENT);
    }

    public static void showDeleteTeacherDialog(Window owner) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Teacher");
        dialog.setContentText("Enter the ID of the teacher you want to delete.");
        dialog.initOwner(owner);
        dialog.setDialogPane((DialogPane) ControllerResource.ADMIN_DELETE_TEACHER_DIALOG.getContent());

        dialog.showAndWait()
                .ifPresent(buttonType -> {
                    if (buttonType == ButtonType.OK) {
                        DeleteTeacherDialog content = ControllerResource.ADMIN_DELETE_TEACHER_DIALOG.getController();
                        content.deleteTeacher();
                    } else {
                        dialog.close();
                    }
                });
    }

    private void deleteTeacher() {
        int parsedId;
        try {
            parsedId = Integer.parseInt(teacherId.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidInput("Invalid ID.", "Please enter a valid ID.");
            return;
        }

        if (teacherId.getText() == null || teacherId.getText().isBlank()) {
            showErrorOnInvalidInput("Enter a value.", "Please enter the id you want to delete");
            return;
        }
        Queries.deleteFromTableUsing(Schema.Tables.TEACHER, Teacher.Column.ID, parsedId);
    }

    private void showErrorOnInvalidInput(String title, String description) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(description);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                showDeleteTeacherDialog(null);
            } else {
                alert.close();
            }
        });
    }
}
