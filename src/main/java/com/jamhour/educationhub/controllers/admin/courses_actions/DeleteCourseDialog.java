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

import java.util.Optional;

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

        if (courseId.getText() == null || courseId.getText().isBlank()) {
            showErrorOnInvalidInput("Enter a value.", "Please enter the id you want to delete");
            return;
        }

        int parsedId;
        try {
            parsedId = Integer.parseInt(courseId.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidInput("Invalid ID.", "Please enter a valid ID.");
            return;
        }

        Optional<Course> courseInDb = Queries.getFromTableUsing(
                Schema.Tables.COURSE,
                Course.Column.ID,
                parsedId
        );

        if (courseInDb.isEmpty()) {
            showErrorOnInvalidInput("Course not found.", STR."Course with ID \{parsedId} not found.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this course?");
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                Queries.deleteFromTableUsing(
                        Schema.Tables.COURSE,
                        Course.Column.ID,
                        parsedId
                );

                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Success");
                alert2.setHeaderText(null);
                alert2.setContentText("Course deleted successfully.");
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
                showDeleteCourseDialog(null);
            } else {
                alert.close();
            }
        });
    }
}
