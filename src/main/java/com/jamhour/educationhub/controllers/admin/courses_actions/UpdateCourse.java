package com.jamhour.educationhub.controllers.admin.courses_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.jamhour.data.Course;
import com.jamhour.data.Teacher;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.Optional;
import java.util.regex.Pattern;

public class UpdateCourse {

    private static final Pattern integerVal = Pattern.compile("\\d+");

    @FXML
    private Message errorMessage;
    @FXML
    private Tile courseIdTile;

    @FXML
    private VBox courseInfoVBox;
    @FXML
    private Message courseInfoMessage;
    @FXML
    private TextField courseIdTextFieldInUpdate;
    @FXML
    private TextField courseNameTextField;
    @FXML
    private TextField teacherIdTextField;

    @FXML
    private Button updateCourseButton;

    private final TextField courseIdTextFieldInInputGroup = new TextField();

    @FXML
    public void initialize() {
        errorMessage.getStyleClass().add(Styles.DANGER);
        courseInfoMessage.getStyleClass().add(Styles.ACCENT);

        createCourseTileAction();
    }

    private void createCourseTileAction() {
        Button search = new Button("Search");


        courseIdTextFieldInInputGroup.textProperty().addListener(((_, _, newValue) -> {
            if (newValue == null) {
                return;
            }
            // TODO change the message for the message object when there's an error in the input for example a string has been entered
            courseIdTextFieldInInputGroup.pseudoClassStateChanged(Styles.STATE_DANGER, !integerVal.matcher(newValue).matches());
        }));

        courseIdTile.setAction(
                new InputGroup(
                        courseIdTextFieldInInputGroup,
                        search
                )
        );


        search.setOnAction(_ -> {
                    Animations.pulse(search).playFromStart();

                    int courseId;
                    try {
                        courseId = Integer.parseInt(courseIdTextFieldInInputGroup.getText());
                    } catch (NumberFormatException e) {
                        courseId = -1;
                    }

                    Queries.<Course>getFromTableUsing(Schema.Tables.COURSE, Course.Column.ID, courseId)
                            .ifPresentOrElse(
                                    this::showCourseInfoVBox,
                                    () -> errorMessage.setVisible(true)
                            );
                }
        );
    }

    private void showCourseInfoVBox(Course course) {
        errorMessage.setVisible(false);

        courseInfoVBox.setVisible(true);


        courseNameTextField.setText(course.name());
        courseIdTextFieldInUpdate.setText(String.valueOf(course.id()));
        teacherIdTextField.setText(String.valueOf(course.teacherId()));


        updateCourseButton.setOnAction(_ -> updateCourseInfoInDatabase(course));

    }

    private void updateCourseInfoInDatabase(Course course) {

        int teacherId;
        try {
            teacherId = Integer.parseInt(teacherIdTextField.getText());
            Queries.getFromTableUsing(Schema.Tables.TEACHER, Teacher.Column.ID, teacherId)
                    .orElseThrow(NumberFormatException::new);

        } catch (NumberFormatException e) {
            Alert experienceAlert = new Alert(Alert.AlertType.ERROR);
            experienceAlert.setTitle("Invalid teacher id");
            experienceAlert.setHeaderText("Please enter a valid teacher id.");
            experienceAlert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Update Course");
        alert.setHeaderText("Are you sure you want to update this course?");
        alert.setContentText("This action cannot be undone.");
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> buttonType = alert.showAndWait();
        courseInfoVBox.setVisible(false);


        if (buttonType.isPresent() && buttonType.get() != ButtonType.OK) {
            return;
        }


        // TODO add a helper method in the course class to handle this process
        Queries.updateTableUsing(
                Schema.Tables.COURSE,
                Course.Column.TEACHER_ID,
                teacherId,
                Course.Column.ID,
                course.id()
        );

        Queries.updateTableUsing(
                Schema.Tables.COURSE,
                Course.Column.NAME,
                courseNameTextField.getText(),
                Course.Column.ID,
                course.id()
        );


    }

    public static void updateCourseInfoInDatabase(Window owner) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Update Course");
        dialog.initOwner(owner);
        dialog.setDialogPane((DialogPane) ControllerResource.ADMIN_UPDATE_COURSE_DIALOG.getContent());
        dialog.showAndWait();
    }
}
