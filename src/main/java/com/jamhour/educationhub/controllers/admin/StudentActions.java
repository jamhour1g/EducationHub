package com.jamhour.educationhub.controllers.admin;

import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StudentActions {

    @FXML
    private Tile searchTile;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn phoneColumn;
    @FXML
    private TableColumn emailColumn;
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
}
