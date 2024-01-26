package com.jamhour.educationhub.controllers.admin.teacher_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.jamhour.data.Course;
import com.jamhour.data.Enrollment;
import com.jamhour.data.Teacher;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ViewCoursesForTeacherDialog {

    private static final Pattern compile = Pattern.compile("\\d+");

    @FXML
    private Message errorMessage;
    @FXML
    private Tile teacherIdTile;
    @FXML
    private VBox coursesVBox;
    @FXML
    private Message teacherInfoMessage;

    @FXML
    private TableView<CourseTableEntry> courseTableView;
    @FXML
    private TableColumn<CourseTableEntry, Integer> courseIdColumn;
    @FXML
    private TableColumn<CourseTableEntry, String> courseNameColumn;
    @FXML
    private TableColumn<CourseTableEntry, Integer> numberOfStudentColumn;

    private Teacher teacher;

    @FXML
    public void initialize() {
        errorMessage.getStyleClass().add(Styles.DANGER);
        teacherInfoMessage.getStyleClass().add(Styles.ACCENT);

        courseIdColumn.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
        numberOfStudentColumn.setCellValueFactory(cellData -> cellData.getValue().getNumberOfStudents().asObject());

        courseTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        courseTableView.getSelectionModel().selectFirst();

        createStudentTileAction();
    }

    private void createStudentTileAction() {
        Button search = new Button("Search");
        TextField studentIdTextField = new TextField();


        studentIdTextField.textProperty().addListener(((_, _, newValue) -> {
            if (newValue == null) {
                return;
            }
            // TODO change the message for the message object when there's an error in the input for example a string has been entered
            studentIdTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !compile.matcher(newValue).matches());
        }));

        teacherIdTile.setAction(
                new InputGroup(
                        studentIdTextField,
                        search
                )
        );


        search.setOnAction(_ -> {
                    Animations.pulse(search).playFromStart();
                    int teacherId;
                    try {
                        teacherId = Integer.parseInt(studentIdTextField.getText());
                    } catch (NumberFormatException e) {
                        teacherId = -1;
                    }

                    Queries.<Teacher>getFromTableUsing(Schema.Tables.TEACHER, Teacher.Column.ID, teacherId)
                            .ifPresentOrElse(teacherInDB -> {
                                        teacher = teacherInDB;
                                        showCoursesVBox();
                                    },
                                    () -> errorMessage.setVisible(true)
                            );
                }
        );
    }

    private void showCoursesVBox() {
        errorMessage.setVisible(false);

        coursesVBox.setVisible(true);


        List<Course> courses = Queries.getAllInTable(Schema.Tables.COURSE);
        List<CourseTableEntry> courseTableEntries = new ArrayList<>();

        // * Note: this can be improved using a select statement
        courses.forEach(course -> {

            if (course.teacherId() == teacher.id()) {
                List<Enrollment> enrollment = Queries.getAllInTableUsing(Schema.Tables.ENROLLMENT, Enrollment.Column.COURSE_ID, course.id());
                courseTableEntries.add(
                        new CourseTableEntry(
                                course.name(),
                                enrollment.size(),
                                course.id()
                        )
                );
            }
        });

        courseTableView.setItems(FXCollections.observableList(courseTableEntries));

    }

    public static void showCoursesForTeacherDialog(Window window) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Courses this teacher teaches");
        dialog.initOwner(window);
        dialog.setDialogPane((DialogPane) ControllerResource.ADMIN_VIEW_COURSES_FOR_TEACHER_DIALOG.getContent());
        dialog.showAndWait();

    }

    @Getter
    public static class CourseTableEntry {

        private final SimpleStringProperty name;
        private final SimpleIntegerProperty id;
        private final SimpleIntegerProperty numberOfStudents;

        public CourseTableEntry(String name, int numberOfStudents, int id) {
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleIntegerProperty(id);
            this.numberOfStudents = new SimpleIntegerProperty(numberOfStudents);
        }

        public void setName(String name) {
            this.name.set(name);
        }


        public void setId(int id) {
            this.id.set(id);
        }


        public String getNameValue() {
            return name.get();
        }

        public int getIdValue() {
            return id.get();
        }

        public void setNumberOfStudents(int numberOfStudents) {
            this.numberOfStudents.set(numberOfStudents);
        }

        public int getNumberOfStudentsValue() {
            return numberOfStudents.get();
        }

    }
}
