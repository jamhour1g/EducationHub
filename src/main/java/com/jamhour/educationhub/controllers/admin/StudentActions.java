package com.jamhour.educationhub.controllers.admin;

import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import com.jamhour.data.Student;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;

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
        Button search = new Button("Search");

        inputGroup.getChildren().addAll(searchField, textField, search);

        searchTile.setAction(inputGroup);
    }

    @Getter
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
