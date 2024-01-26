package com.jamhour.educationhub.controllers.admin.student_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.Popover;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.jamhour.data.Student;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Data;

public class StudentActions {

    @FXML
    private Tile searchTile;

    @FXML
    private TableView<StudentTableEntry> tableView;
    @FXML
    private TableColumn<StudentTableEntry, Integer> idColumn;
    @FXML
    private TableColumn<StudentTableEntry, String> nameColumn;
    @FXML
    private TableColumn<StudentTableEntry, String> phoneColumn;
    @FXML
    private TableColumn<StudentTableEntry, String> emailColumn;

    @FXML
    private Button addStudent;
    @FXML
    private Button deleteStudent;
    @FXML
    private Button updateStudent;
    @FXML
    private Button registerStudent;

    @FXML
    public void initialize() {
        initSearchTile();
        initTable();
    }

    private void initTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        refreshTableContents();
    }

    private void refreshTableContents() {
        tableView.getItems().clear();
        Queries.<Student>getAllInTable(Schema.Tables.STUDENT)
                .forEach(student -> tableView.getItems().add(new StudentTableEntry(student)));
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
    public void addStudent() {
        Animations.pulse(addStudent).playFromStart();
        AddStudentDialog.showAddStudentDialog(addStudent.getScene().getWindow());
        refreshTableContents();
    }

    @FXML
    public void deleteStudent() {
        Animations.pulse(deleteStudent).playFromStart();
        DeleteStudentDialog.showDeleteStudentDialog(deleteStudent.getScene().getWindow());
        refreshTableContents();
    }

    @FXML
    public void updateStudent() {
        Animations.pulse(updateStudent).playFromStart();
        // TODO : update the table view to adjust to the new student updates in DB
        UpdateStudent.updateStudentInfoInDatabase(updateStudent.getScene().getWindow());
        refreshTableContents();
    }

    @FXML
    public void registerStudent() {
        Animations.pulse(registerStudent).playFromStart();
        RegisterStudentDialog.showRegisterStudentDialog(registerStudent.getScene().getWindow());
    }

    private void handleSearch(Button search, TextField textField, ComboBox<String> searchField) {
        Animations.pulse(search).playFromStart();

        if (textField.getText() == null || textField.getText().isBlank()) {
            showErrorOnInvalidSearchInput("Enter a value.", "Please enter the value you want to search for.", textField);
            return;
        }

        switch (searchField.getValue()) {
            case "ID" -> getStudentUsingId(textField);
            case "Name" -> getStudentUsing(Student.Column.NAME, textField);
            case "Phone" -> getStudentUsing(Student.Column.PHONE, textField);
            case "Email" -> getStudentUsing(Student.Column.EMAIL, textField);
        }

    }

    private void getStudentUsingId(TextField textField) {
        int parsedId;
        try {
            parsedId = Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            showErrorOnInvalidSearchInput("Invalid ID.", "Please enter a valid ID.", textField);
            return;
        }

        Queries.<Student>getFromTableUsing(Schema.Tables.STUDENT, Student.Column.ID, parsedId)
                .ifPresentOrElse(
                        student -> {
                            StudentTableEntry studentTableEntry = new StudentTableEntry(student);
                            tableView.getSelectionModel().select(studentTableEntry);
                            tableView.scrollTo(studentTableEntry);
                        },
                        () -> showErrorOnInvalidSearchInput("No results found.", "", textField)
                );
    }

    private void getStudentUsing(Student.Column column, TextField textField) {
        Queries.<Student>getFromTableUsing(Schema.Tables.STUDENT, column, textField.getText())
                .ifPresentOrElse(
                        student -> {
                            StudentTableEntry studentTableEntry = new StudentTableEntry(student);
                            tableView.getSelectionModel().select(studentTableEntry);
                            tableView.scrollTo(studentTableEntry);
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

    public static StudentActions getInstance() {
        return ControllerResource.ADMIN_STUDENT_ACTIONS.getController();
    }

    @Data
    public static class StudentTableEntry {
        private final int id;
        private final String name;
        private final String phone;
        private final String email;

        public StudentTableEntry(Student student) {
            this.id = student.id();
            this.name = student.name();
            this.phone = student.phone();
            this.email = student.email();
        }
    }
}
