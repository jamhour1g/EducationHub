package com.jamhour.educationhub.controllers.admin.teacher_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
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

public class UpdateTeacher {

    private static final Pattern integerVal = Pattern.compile("\\d+");

    @FXML
    private Message errorMessage;
    @FXML
    private Tile teacherIdTile;

    @FXML
    private VBox teacherInfoVBox;
    @FXML
    private Message teacherInfoMessage;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField experienceTextField;
    @FXML
    private TextField salaryTextField;
    @FXML
    private TextField majorTextField;
    @FXML
    private DatePicker dateOfBirthDatePicker;
    @FXML
    private Button updateTeacherButton;

    private final TextField teacherIdTextField = new TextField();

    @FXML
    public void initialize() {
        errorMessage.getStyleClass().add(Styles.DANGER);
        teacherInfoMessage.getStyleClass().add(Styles.ACCENT);

        createTeacherTileAction();
    }

    private void createTeacherTileAction() {
        Button search = new Button("Search");


        teacherIdTextField.textProperty().addListener(((_, _, newValue) -> {
            if (newValue == null) {
                return;
            }
            // TODO change the message for the message object when there's an error in the input for example a string has been entered
            teacherIdTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !integerVal.matcher(newValue).matches());
        }));

        teacherIdTile.setAction(
                new InputGroup(
                        teacherIdTextField,
                        search
                )
        );


        search.setOnAction(_ -> {
                    Animations.pulse(search).playFromStart();
                    int teacherId;
                    try {
                        teacherId = Integer.parseInt(teacherIdTextField.getText());
                    } catch (NumberFormatException e) {
                        teacherId = -1;
                    }

                    Queries.<Teacher>getFromTableUsing(Schema.Tables.TEACHER, Teacher.Column.ID, teacherId)
                            .ifPresentOrElse(
                                    this::showTeacherInfoVBox,
                                    () -> errorMessage.setVisible(true)
                            );
                }
        );
    }

    private void showTeacherInfoVBox(Teacher teacher) {
        errorMessage.setVisible(false);

        teacherInfoVBox.setVisible(true);
        idTextField.setText(String.valueOf(teacher.id()));

        nameTextField.setText(teacher.name());
        phoneTextField.setText(teacher.phone());
        emailTextField.setText(teacher.email());
        experienceTextField.setText(String.valueOf(teacher.experience()));
        salaryTextField.setText(String.valueOf(teacher.salary()));
        majorTextField.setText(teacher.major());
        dateOfBirthDatePicker.setValue(teacher.dateOfBirth());


        updateTeacherButton.setOnAction(_ -> updateTeacherInfoInDatabase(teacher));

    }

    private void updateTeacherInfoInDatabase(Teacher teacher) {

        double salary;
        try {
            salary = Double.parseDouble(salaryTextField.getText());
        } catch (NumberFormatException e) {
            Alert salaryAlert = new Alert(Alert.AlertType.ERROR);
            salaryAlert.setTitle("Invalid salary");
            salaryAlert.setHeaderText("Please enter a valid salary.");
            salaryAlert.showAndWait();
            return;
        }

        int experience;
        try {
            experience = Integer.parseInt(experienceTextField.getText());
        } catch (NumberFormatException e) {
            Alert experienceAlert = new Alert(Alert.AlertType.ERROR);
            experienceAlert.setTitle("Invalid experience");
            experienceAlert.setHeaderText("Please enter a valid experience.");
            experienceAlert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Update teacher");
        alert.setHeaderText("Are you sure you want to update this teacher?");
        alert.setContentText("This action cannot be undone.");
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> buttonType = alert.showAndWait();
        teacherInfoVBox.setVisible(false);


        if (buttonType.isPresent() && buttonType.get() != ButtonType.OK) {
            return;
        }


        // TODO add a helper method in the teacher class to handle this process
        Queries.updateTableUsing(
                Schema.Tables.TEACHER,
                Teacher.Column.NAME,
                nameTextField.getText(),
                Teacher.Column.ID,
                teacher.id()
        );

        Queries.updateTableUsing(
                Schema.Tables.TEACHER,
                Teacher.Column.PHONE,
                phoneTextField.getText(),
                Teacher.Column.ID,
                teacher.id()
        );

        Queries.updateTableUsing(
                Schema.Tables.TEACHER,
                Teacher.Column.EMAIL,
                emailTextField.getText(),
                Teacher.Column.ID,
                teacher.id()
        );

        Queries.updateTableUsing(
                Schema.Tables.TEACHER,
                Teacher.Column.MAJOR,
                majorTextField.getText(),
                Teacher.Column.ID,
                teacher.id()
        );

        Queries.updateTableUsing(
                Schema.Tables.TEACHER,
                Teacher.Column.SALARY,
                salary,
                Teacher.Column.ID,
                teacher.id()
        );

        Queries.updateTableUsing(
                Schema.Tables.TEACHER,
                Teacher.Column.EXPERIENCE,
                experience,
                Teacher.Column.ID,
                teacher.id()
        );

    }

    public static void updateTeacherInfoInDatabase(Window owner) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Update Teacher");
        dialog.initOwner(owner);
        dialog.setDialogPane((DialogPane) ControllerResource.ADMIN_UPDATE_TEACHER_DIALOG.getContent());
        dialog.showAndWait();
    }
}
