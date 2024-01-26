package com.jamhour.educationhub.controllers.admin.teacher_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.Popover;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.jamhour.data.Teacher;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TeacherActions {

    @FXML
    private Tile searchTile;

    @FXML
    private TableView<TeacherTableEntry> teacherTableView;
    @FXML
    private TableColumn<TeacherTableEntry, Integer> teacherIdColumn;
    @FXML
    private TableColumn<TeacherTableEntry, String> teacherNameColumn;
    @FXML
    private TableColumn<TeacherTableEntry, String> teacherPhoneColumn;
    @FXML
    private TableColumn<TeacherTableEntry, String> teacherEmailColumn;
    @FXML
    private TableColumn<TeacherTableEntry, String> teacherMajorColumn;
    @FXML
    private TableColumn<TeacherTableEntry, Integer> teacherExperienceColumn;
    @FXML
    private TableColumn<TeacherTableEntry, String> teacherDateOfBirthColumn;
    @FXML
    private TableColumn<TeacherTableEntry, Double> teacherSalaryColumn;

    @FXML
    private Button addTeacher;
    @FXML
    private Button deleteTeacher;
    @FXML
    private Button updateTeacher;
    @FXML
    private Button viewCourses;

    @FXML
    public void initialize() {
        initSearchTile();
        initTable();
    }

    private void initTable() {
        teacherIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        teacherPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        teacherEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        teacherMajorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));
        teacherExperienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));
        teacherDateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        teacherSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        teacherTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        Queries.<Teacher>getAllInTable(Schema.Tables.TEACHER)
                .forEach(teacher -> teacherTableView.getItems().add(new TeacherTableEntry(teacher)));
    }

    private void initSearchTile() {
        InputGroup inputGroup = new InputGroup();

        ComboBox<String> searchField = new ComboBox<>(
                FXCollections.observableArrayList(
                        "ID",
                        "Name",
                        "Phone",
                        "Email"
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
    public void addTeacher() {
        Animations.pulse(addTeacher).playFromStart();
        AddTeacherDialog.showAddTeacherDialog(addTeacher.getScene().getWindow());
    }

    @FXML
    public void deleteTeacher() {
        Animations.pulse(deleteTeacher).playFromStart();
        DeleteTeacherDialog.showDeleteTeacherDialog(deleteTeacher.getScene().getWindow());
    }

    @FXML
    public void updateTeacher() {
        Animations.pulse(updateTeacher).playFromStart();
        // TODO : update the table view to adjust to the new teacher updates in DB
        UpdateTeacher.updateTeacherInfoInDatabase(updateTeacher.getScene().getWindow());
    }

    @FXML
    public void viewCourses() {
        Animations.pulse(viewCourses).playFromStart();
        ViewCoursesForTeacherDialog.showCoursesForTeacherDialog(viewCourses.getScene().getWindow());
    }

    private void handleSearch(Button search, TextField textField, ComboBox<String> searchField) {
        Animations.pulse(search).playFromStart();

        if (textField.getText() == null || textField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Enter a value.", "Please enter the value you want to search for.", textField);
            return;
        }

        switch (searchField.getValue()) {
            case "ID" -> getTeacherUsingId(textField);
            case "Name" -> getTeacherUsing(Teacher.Column.NAME, textField);
            case "Phone" -> getTeacherUsing(Teacher.Column.PHONE, textField);
            case "Email" -> getTeacherUsing(Teacher.Column.EMAIL, textField);
        }

    }

    private void getTeacherUsingId(TextField textField) {
        int parsedId;
        try {
            parsedId = Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidSearchInput("Invalid ID.", "Please enter a valid ID.", textField);
            return;
        }

        Queries.<Teacher>getFromTableUsing(Schema.Tables.TEACHER, Teacher.Column.ID, parsedId)
                .ifPresentOrElse(
                        teacher -> {
                            TeacherTableEntry teacherTableEntry = new TeacherTableEntry(teacher);
                            teacherTableView.getSelectionModel().select(teacherTableEntry);
                            teacherTableView.scrollTo(teacherTableEntry);
                        },
                        () -> showErrorOnInvalidSearchInput("No results found.", "", textField)
                );
    }

    private void getTeacherUsing(Teacher.Column column, TextField textField) {
        Queries.<Teacher>getFromTableUsing(Schema.Tables.TEACHER, column, textField.getText())
                .ifPresentOrElse(
                        teacher -> {
                            TeacherTableEntry teacherTableEntry = new TeacherTableEntry(teacher);
                            teacherTableView.getSelectionModel().select(teacherTableEntry);
                            teacherTableView.scrollTo(teacherTableEntry);
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

    @Data
    public static class TeacherTableEntry {
        private final int id;
        private final String name;
        private final String phone;
        private final String email;
        private final String major;
        private final String dateOfBirth;
        private final int experience;
        private final double salary;


        public TeacherTableEntry(Teacher teacher) {
            this.id = teacher.id();
            this.name = teacher.name();
            this.phone = teacher.phone();
            this.email = teacher.email();
            this.experience = teacher.experience();
            this.major = teacher.major();
            this.dateOfBirth = teacher.dateOfBirth().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
            this.salary = teacher.salary();
        }
    }
}
