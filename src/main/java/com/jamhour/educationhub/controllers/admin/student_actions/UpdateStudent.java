package com.jamhour.educationhub.controllers.admin.student_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.jamhour.data.Student;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.Optional;
import java.util.regex.Pattern;

public class UpdateStudent {

    private static final Pattern compile = Pattern.compile("\\d+");

    @FXML
    private Message errorMessage;
    @FXML
    private Tile studentIdTile;

    @FXML
    private VBox studentInfoVBox;
    @FXML
    private Message studentInfoMessage;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button updateStudentButton;

    private final TextField studentIdTextField = new TextField();

    @FXML
    public void initialize() {
        errorMessage.getStyleClass().add(Styles.DANGER);
        studentInfoMessage.getStyleClass().add(Styles.ACCENT);

        createStudentTileAction();
    }

    private void createStudentTileAction() {
        Button search = new Button("Search");


        studentIdTextField.textProperty().addListener(((_, _, newValue) -> {
            if (newValue == null) {
                return;
            }
            // TODO change the message for the message object when there's an error in the input for example a string has been entered
            studentIdTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !compile.matcher(newValue).matches());
        }));

        studentIdTile.setAction(
                new InputGroup(
                        studentIdTextField,
                        search
                )
        );


        search.setOnAction(_ -> {
                    Animations.pulse(search).playFromStart();
                    int studentId;
                    try {
                        studentId = Integer.parseInt(studentIdTextField.getText());
                    } catch (NumberFormatException e) {
                        studentId = -1;
                    }

                    Queries.<Student>getFromTableUsing(Schema.Tables.STUDENT, Student.Column.ID, studentId)
                            .ifPresentOrElse(
                                    this::showStudentInfoVBox,
                                    () -> errorMessage.setVisible(true)
                            );
                }
        );
    }

    private void showStudentInfoVBox(Student student) {
        errorMessage.setVisible(false);

        studentInfoVBox.setVisible(true);
        idTextField.setText(String.valueOf(student.id()));

        nameTextField.setText(student.name());
        phoneTextField.setText(student.phone());
        emailTextField.setText(student.email());

        updateStudentButton.setOnAction(_ -> updateStudentInfoInDatabase(student));

    }

    private void updateStudentInfoInDatabase(Student student) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Update Student");
        alert.setHeaderText("Are you sure you want to update this student?");
        alert.setContentText("This action cannot be undone.");
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> buttonType = alert.showAndWait();
        studentInfoVBox.setVisible(false);


        if (buttonType.isPresent() && buttonType.get() != ButtonType.OK) {
            return;
        }

        // TODO add a helper method in the student class to handle this process
        Queries.updateTableUsing(
                Schema.Tables.STUDENT,
                Student.Column.NAME,
                nameTextField.getText(),
                Student.Column.ID,
                student.id()
        );

        Queries.updateTableUsing(
                Schema.Tables.STUDENT,
                Student.Column.EMAIL,
                emailTextField.getText(),
                Student.Column.ID,
                student.id()
        );

        Queries.updateTableUsing(
                Schema.Tables.STUDENT,
                Student.Column.PHONE,
                phoneTextField.getText(),
                Student.Column.ID,
                student.id()
        );

    }

    public static void updateStudentInfoInDatabase(Window owner) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Update Student");
        dialog.initOwner(owner);
        dialog.setDialogPane((DialogPane) ControllerResource.ADMIN_UPDATE_STUDENT_DIALOG.getContent());
        dialog.showAndWait();
    }
}
