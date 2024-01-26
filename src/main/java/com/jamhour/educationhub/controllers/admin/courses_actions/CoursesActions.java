package com.jamhour.educationhub.controllers.admin.courses_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.Popover;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.jamhour.data.Course;
import com.jamhour.data.Enrollment;
import com.jamhour.data.Exam;
import com.jamhour.data.Teacher;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

public class CoursesActions {

    @FXML
    private Tile searchTile;

    @FXML
    private TableView<CourseTableEntry> courseTableView;
    @FXML
    private TableColumn<CourseTableEntry, Integer> courseIdColumn;
    @FXML
    private TableColumn<CourseTableEntry, String> courseNameColumn;
    @FXML
    private TableColumn<CourseTableEntry, Integer> numberOfEnrollmentsColumn;
    @FXML
    private TableColumn<CourseTableEntry, Integer> numberOfExamsColumn;
    @FXML
    private TableColumn<CourseTableEntry, Integer> teacherIdColumn;
    @FXML
    private TableColumn<CourseTableEntry, String> teacherNameColumn;

    @FXML
    private Button addCourseButton;
    @FXML
    private Button deleteCourseButton;
    @FXML
    private Button updateCourseButton;

    @FXML
    public void initialize() {
        initSearchTile();
        initTable();
    }

    private void initTable() {
        courseIdColumn.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
        numberOfEnrollmentsColumn.setCellValueFactory(cellData -> cellData.getValue().getNumberOfStudents().asObject());
        numberOfExamsColumn.setCellValueFactory(cellData -> cellData.getValue().getNumberOfExams().asObject());
        teacherIdColumn.setCellValueFactory(cellData -> cellData.getValue().getTeacherId().asObject());
        teacherNameColumn.setCellValueFactory(cellData -> cellData.getValue().getTeacherName());

        courseTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        Queries.<Course>getAllInTable(Schema.Tables.COURSE)
                .forEach(course -> courseTableView.getItems().add(new CourseTableEntry(course)));
    }

    private void initSearchTile() {
        InputGroup inputGroup = new InputGroup();

        ComboBox<String> searchField = new ComboBox<>(
                FXCollections.observableArrayList(
                        "ID",
                        "Name",
                        "Teacher ID"
                )
        );
        searchField.getSelectionModel().selectFirst();

        TextField textField = new TextField();
        textField.setPrefWidth(220);
        textField.setPromptText("Enter a value.");
        textField.textProperty().addListener(
                (_, _, _) -> textField.pseudoClassStateChanged(Styles.STATE_DANGER, false)
        );

        Button search = new Button("Search");
        search.setOnAction(_ -> handleSearch(search, textField, searchField));

        inputGroup.getChildren().addAll(searchField, textField, search);

        searchTile.setAction(inputGroup);
    }

    @FXML
    public void addCourse() {
        Animations.pulse(addCourseButton).playFromStart();
        AddCourseDialog.showAddCourseDialog(addCourseButton.getScene().getWindow());
    }

    @FXML
    public void deleteCourse() {
        Animations.pulse(deleteCourseButton).playFromStart();
        DeleteCourseDialog.showDeleteCourseDialog(deleteCourseButton.getScene().getWindow());
    }

    @FXML
    public void updateCourse() {
        Animations.pulse(updateCourseButton).playFromStart();
        // TODO : update the table view to adjust to the new course updates in DB
        UpdateCourse.updateCourseInfoInDatabase(updateCourseButton.getScene().getWindow());
    }

    private void handleSearch(Button search, TextField textField, ComboBox<String> searchField) {
        Animations.pulse(search).playFromStart();

        if (textField.getText() == null || textField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Enter a value.", "Please enter the value you want to search for.", textField);
            return;
        }

        switch (searchField.getValue()) {
            case "ID" -> getCourseUsingId(textField);
            case "Name" -> getCourseUsingName(textField);
            case "Teacher ID" -> getTeacherUsingId(textField);
        }

    }

    private void getCourseUsingId(TextField textField) {
        int parsedId;
        try {
            parsedId = Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidSearchInput("Invalid ID.", "Please enter a valid ID.", textField);
            return;
        }

        Queries.<Course>getFromTableUsing(Schema.Tables.COURSE, Course.Column.ID, parsedId)
                .ifPresentOrElse(
                        course -> {
                            CourseTableEntry courseTableEntry = new CourseTableEntry(course);
                            courseTableView.getSelectionModel().select(courseTableEntry);
                            courseTableView.scrollTo(courseTableEntry);
                        },
                        () -> showErrorOnInvalidSearchInput("No results found.", "", textField)
                );
    }

    private void getTeacherUsingId(TextField textField) {
        int parsedId;
        try {
            parsedId = Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidSearchInput("Invalid ID.", "Please enter a valid ID.", textField);
            return;
        }

        Queries.<Course>getFromTableUsing(Schema.Tables.COURSE, Course.Column.TEACHER_ID, parsedId)
                .ifPresentOrElse(
                        course -> {
                            CourseTableEntry courseTableEntry = new CourseTableEntry(course);
                            courseTableView.getSelectionModel().select(courseTableEntry);
                            courseTableView.scrollTo(courseTableEntry);
                        },
                        () -> showErrorOnInvalidSearchInput("No results found.", "", textField)
                );
    }

    private void getCourseUsingName(TextField textField) {
        Queries.<Course>getFromTableUsing(Schema.Tables.COURSE, Course.Column.NAME, textField.getText())
                .ifPresentOrElse(
                        course -> {
                            CourseTableEntry courseTableEntry = new CourseTableEntry(course);
                            courseTableView.getSelectionModel().select(courseTableEntry);
                            courseTableView.scrollTo(courseTableEntry);
                        },
                        () -> showErrorOnInvalidSearchInput("No results found.", "", textField)
                );
    }

    private static void showErrorOnInvalidSearchInput(String title, String description, TextField textField) {

        Message content = new Message(title, description);
        content.getStyleClass().add(Styles.DANGER);
        content.setPrefHeight(50);
        content.setPrefWidth(500);

        Popover popover = new Popover(content);
        popover.setArrowLocation(Popover.ArrowLocation.TOP_CENTER);
        popover.show(textField);

        textField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
        Animations.flash(textField).playFromStart();
    }

    @Getter
    public static class CourseTableEntry {

        private final SimpleStringProperty name;
        private final SimpleIntegerProperty id;
        private final SimpleIntegerProperty teacherId;
        private final SimpleStringProperty teacherName;
        private final SimpleIntegerProperty numberOfStudents;
        private final SimpleIntegerProperty numberOfExams;

        public CourseTableEntry(Course course) {
            this.id = new SimpleIntegerProperty(course.id());
            this.name = new SimpleStringProperty(course.name());
            Optional<Teacher> teacher = Queries.getFromTableUsing(
                    Schema.Tables.TEACHER,
                    Teacher.Column.ID,
                    course.teacherId()
            );
            String teacherName = teacher.map(Teacher::name).orElse("");

            this.teacherName = new SimpleStringProperty(teacherName);
            this.teacherId = new SimpleIntegerProperty(teacher.orElseThrow().id());
            int numberOfStudents =
                    Queries.<Enrollment>getAllInTableUsing(
                            Schema.Tables.ENROLLMENT,
                            Enrollment.Column.COURSE_ID,
                            course.id()
                    ).size();
            this.numberOfStudents = new SimpleIntegerProperty(numberOfStudents);

            int numberOfExams =
                    Queries.<Exam>getAllInTableUsing(
                            Schema.Tables.EXAM,
                            Exam.Column.COURSE_ID,
                            course.id()
                    ).size();
            this.numberOfExams = new SimpleIntegerProperty(numberOfExams);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CourseTableEntry that)) return false;
            return name.get().equals(that.name.get()) &&
                   id.get() == that.id.get() &&
                   teacherId.get() == that.teacherId.get() &&
                   teacherName.get().equals(that.teacherName.get()) &&
                   numberOfStudents.get() == that.numberOfStudents.get() &&
                   numberOfExams.get() == that.numberOfExams.get();
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                    name.get(),
                    id.get(),
                    teacherId.get(),
                    teacherName.get(),
                    numberOfStudents.get(),
                    numberOfExams.get()
            );
        }

    }
}
