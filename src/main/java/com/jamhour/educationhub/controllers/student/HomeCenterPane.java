package com.jamhour.educationhub.controllers.student;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.jamhour.data.*;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.App;
import com.jamhour.educationhub.controllers.ControllerResource;
import com.jamhour.educationhub.controllers.SettingsDialog;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;

public class HomeCenterPane {
    private static final ControllerResource controller = ControllerResource.STUDENT_HOME_CENTER_PANE;

    @FXML
    private Button settings;

    @FXML
    private FlowPane coursesFlowPane;
    @FXML
    private FlowPane examsFlowPane;
    @FXML
    private Text welcomeText;


    public void initialize() {
        settings.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.ACCENT
        );
        settings.setMnemonicParsing(true);


        Student student = StudentHomeScreen.getInstance().getStudent();
        welcomeText.setText(STR."\{welcomeText.getText()}, \{student.name()}");

        List<Enrollment> enrollments = Queries.getAllInTableUsing(
                Schema.Tables.ENROLLMENT,
                Enrollment.Column.STUDENT_ID,
                student.id()
        );
        enrollments.forEach(this::addCourses);

    }

    private void addCourses(Enrollment enrollment) {
        Optional<Course> courseFromDatabase = Queries.getFromTableUsing(
                Schema.Tables.COURSE,
                Course.Column.ID,
                enrollment.courseId()
        );

        courseFromDatabase.ifPresent(course -> {
            coursesFlowPane.getChildren().add(createCardForCourse(enrollment, course, getTeacherForCourse(course)));
            addExams(course);
        });
    }

    private void addExams(Course course) {
        List<Exam> examList = Queries.getAllInTableUsing(
                Schema.Tables.EXAM,
                Exam.Column.COURSE_ID,
                course.id()
        );

        examList.forEach(exam -> {
            Optional<ExamResult> examResult = Queries.getFromTableUsing(
                    Schema.Tables.EXAM_RESULT,
                    ExamResult.Column.EXAM_ID,
                    exam.id()
            );

            Card examCard = createExamCard(course, exam, examResult.orElseThrow());
            examsFlowPane.getChildren().add(examCard);
        });
    }

    private static Card createExamCard(Course course, Exam exam, ExamResult examResult) {
        Card examCard = new Card();

        examCard.setPrefSize(263, 183);
        examCard.setHeader(new Tile(STR."\{exam.name()}", STR."Course - \{course.name()}"));
        examCard.setSubHeader(
                new TextFlow(
                        new Text(
                                STR."""
                                \{exam.description()}
                                Exam time - \{
                                        exam
                                                .examDateTime()
                                                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))}
                                """
                        )
                )
        );

        examCard.getStyleClass().add(Styles.INTERACTIVE);
        examCard.setOnMouseClicked(_ -> Animations.pulse(examCard).playFromStart());

        examCard.setFooter(
                new TextFlow(
                        new Text(STR."Result - \{examResult.grade()}\n")
                )
        );
        return examCard;
    }

    private static Card createCardForCourse(Enrollment enrollment, Course course, Teacher teacher) {
        Card courseCard = new Card();

        courseCard.setHeader(
                new Tile(
                        course.name(),
                        STR."""
                        Teacher - \{teacher.name()}
                        Enrollment status - \{
                                enrollment
                                        .status()
                                        .toString()
                                        .toLowerCase()
                                }
                        """
                )
        );
        courseCard.getStyleClass().add(Styles.INTERACTIVE);
        courseCard.setPrefSize(263, 183);
        courseCard.setFooter(createSeeExamFooterForCard(course, courseCard));

        return courseCard;
    }

    private static BorderPane createSeeExamFooterForCard(Course course, Card courseCard) {
        BorderPane footer = new BorderPane();

        Button seeExams = new Button("See Exams");
        seeExams.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED,
                Styles.ACCENT
        );
        seeExams.setMnemonicParsing(true);
        seeExams.setOnAction(_ -> openSeeExamDialog(course, courseCard, seeExams));

        footer.setRight(seeExams);

        return footer;
    }

    private static void openSeeExamDialog(Course course, Card courseCard, Button seeExams) {
        Animations.pulse(seeExams).playFromStart();
        Animations.pulse(courseCard).playFromStart();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(STR."Course - \{course.name()} available exams");
        dialog.setHeaderText(STR."Course - \{course.name()} available exams");
        dialog.setContentText("Here's a list of all available exams for this course");
        dialog.initOwner(App.getInstance().getWindow());

        Parent coursesDialogContent = ControllerResource.COURSES_DIALOG.getContent();
        CourseDialog coursesDialogController = ControllerResource.COURSES_DIALOG.getController();
        coursesDialogController.setCourse(course);
        coursesDialogController.initialize();


        dialog.getDialogPane().setContent(coursesDialogContent);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait().ifPresent(_ -> dialog.close());
    }

    private static Teacher getTeacherForCourse(Course course) {
        return (Teacher) Queries.getFromTableUsing(Schema.Tables.TEACHER, Teacher.Column.ID, course.teacherId()).orElseThrow();
    }

    @FXML
    public void openSettings() {
        Animations.pulse(settings).playFromStart();
        SettingsDialog.showSettingsDialog(settings.getScene().getWindow());
    }

    public static HomeCenterPane getInstance() {
        return controller.getController();
    }

}
