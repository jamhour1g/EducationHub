package com.jamhour.educationhub.controllers.admin;

import atlantafx.base.controls.Tile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
}
