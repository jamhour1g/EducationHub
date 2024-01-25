package com.jamhour.educationhub.controllers.admin.student_actions;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.jamhour.data.Course;
import com.jamhour.data.Enrollment;
import com.jamhour.data.Student;
import com.jamhour.data.Teacher;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class RegisterStudentDialog {

    private static final Pattern compile = Pattern.compile("\\d+");

    @FXML
    private Message errorMessage;
    @FXML
    private Tile studentIdTile;
    @FXML
    private VBox coursesVBox;
    @FXML
    private Message studentInfoMessage;
    @FXML
    private TableView<CourseTableEntry> courseTableView;
    @FXML
    private TableColumn<CourseTableEntry, String> teacherNameColumn;
    @FXML
    private TableColumn<CourseTableEntry, Boolean> checkBoxColumn;
    @FXML
    private TableColumn<CourseTableEntry, Integer> courseIdColumn;
    @FXML
    private TableColumn<CourseTableEntry, String> courseNameColumn;
    @FXML
    private Button registerStudentButton;

    private Student student;

    @FXML
    public void initialize() {
        errorMessage.getStyleClass().add(Styles.DANGER);
        studentInfoMessage.getStyleClass().add(Styles.ACCENT);

        teacherNameColumn.setCellValueFactory(cellData -> cellData.getValue().getTeacherName());
        courseIdColumn.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());

        var selectAll = new CheckBox();
        selectAll.setOnAction(evt -> {
            courseTableView.getItems().forEach(item -> item.setRegistered(selectAll.isSelected()));
            evt.consume();
        });

        checkBoxColumn.setGraphic(selectAll);
        checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().getRegistered());
        // TODO find a more cleaner/clever way to do this
        checkBoxColumn.setCellFactory(_ -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setAlignment(Pos.CENTER);
                checkBox.selectedProperty().addListener((_, _, newValue) -> {
                    CourseTableEntry entry = getTableRow().getItem();
                    entry.setRegistered(newValue);
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    setGraphic(checkBox);
                }
            }
        });
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
                            .ifPresentOrElse(studentInDB -> {
                                        student = studentInDB;
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
            Optional<Teacher> teacher = Queries.getFromTableUsing(Schema.Tables.TEACHER, Teacher.Column.ID, course.teacherId());
            List<Enrollment> studentEnrollments = Queries.<Enrollment>getAllInTableUsing(Schema.Tables.ENROLLMENT, Enrollment.Column.COURSE_ID, course.id())
                    .stream()
                    .filter(enrollment -> enrollment.studentId() == student.id())
                    .toList();

            if (studentEnrollments.isEmpty()) {
                courseTableEntries.add(
                        new CourseTableEntry(
                                course.name(),
                                teacher.map(Teacher::name).orElse(""),
                                false,
                                course.id()
                        )
                );
            }

        });

        courseTableView.setItems(FXCollections.observableList(courseTableEntries));

    }

    @FXML
    public void registerStudent() {
        Animations.pulse(registerStudentButton).playFromStart();
        courseTableView.getItems()
                .stream()
                .filter(CourseTableEntry::isRegisteredValue)
                .forEach(courseTableEntry ->
                        Queries.insertIntoTable(
                                Schema.Tables.ENROLLMENT,
                                new Enrollment(
                                        Enrollment.EnrollmentStatus.ACTIVE,
                                        true,
                                        courseTableEntry.getIdValue(),
                                        student.id()
                                )
                        )
                );
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Register Student");
        alert.setHeaderText("Student successfully registered");
        alert.showAndWait();
    }

    public static void showRegisterStudentDialog(Window window) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Register Student");
        dialog.initOwner(window);
        dialog.setDialogPane((DialogPane) ControllerResource.ADMIN_REGISTER_STUDENT_DIALOG.getContent());
        dialog.showAndWait();

    }

    @Getter
    public static class CourseTableEntry {

        private final SimpleStringProperty name;
        private final SimpleStringProperty teacherName;
        private final SimpleIntegerProperty id;
        private final SimpleBooleanProperty registered;


        public CourseTableEntry(String name, String teacherName, boolean registered, int id) {
            this.name = new SimpleStringProperty(name);
            this.teacherName = new SimpleStringProperty(teacherName);
            this.id = new SimpleIntegerProperty(id);
            this.registered = new SimpleBooleanProperty(registered);
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public void setTeacherName(String teacherName) {
            this.teacherName.set(teacherName);
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public void setRegistered(boolean registered) {
            this.registered.set(registered);
        }

        public String getNameValue() {
            return name.get();
        }

        public String getTeacherNameValue() {
            return teacherName.get();
        }

        public int getIdValue() {
            return id.get();
        }

        public boolean isRegisteredValue() {
            return registered.get();
        }

    }
}
