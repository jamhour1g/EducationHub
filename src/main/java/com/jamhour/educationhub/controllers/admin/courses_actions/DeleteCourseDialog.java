package com.jamhour.educationhub.controllers.admin.courses_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import com.jamhour.data.Course;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;

public class DeleteCourseDialog {

    @FXML
    private Message message;
    @FXML
    private TextField courseId;

    @FXML
    public void initialize() {
        message.getStyleClass().add(Styles.ACCENT);
    }

    public static void showDeleteCourseDialog(Window owner) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Course");
        dialog.setContentText("Enter the ID for the course you want to delete.");
        dialog.initOwner(owner);
        dialog.setDialogPane((DialogPane) ControllerResource.ADMIN_DELETE_COURSE_DIALOG.getContent());

        dialog.showAndWait()
                .ifPresent(buttonType -> {
                    if (buttonType == ButtonType.OK) {
                        DeleteCourseDialog content = ControllerResource.ADMIN_DELETE_COURSE_DIALOG.getController();
                        content.deleteCourse();
                    } else {
                        dialog.close();
                    }
                });
    }

    private void deleteCourse() {
        int parsedId;
        try {
            parsedId = Integer.parseInt(courseId.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidInput("Invalid ID.", "Please enter a valid ID.");
            return;
        }

        if (courseId.getText() == null || courseId.getText().isBlank()) {
            showErrorOnInvalidInput("Enter a value.", "Please enter the id you want to delete");
            return;
        }
        Queries.deleteFromTableUsing(Schema.Tables.COURSE, Course.Column.ID, parsedId);
    }

    private void showErrorOnInvalidInput(String title, String description) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(description);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                showDeleteCourseDialog(null);
            } else {
                alert.close();
            }
        });
    }
}
