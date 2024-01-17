package com.jamhour.educationhub.controllers.student;

import com.jamhour.data.Course;
import com.jamhour.data.Enrollment;
import com.jamhour.data.Exam;
import com.jamhour.data.ExamResult;
import com.jamhour.database.Schema;
import com.jamhour.database.queries.Queries;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Exams {
    private static final ControllerResource controller = ControllerResource.STUDENT_EXAMS;

    @FXML
    private TableView<TableViewDataEntry> tableView;
    @FXML
    private TableColumn<TableViewDataEntry, String> courseColumn;
    @FXML
    private TableColumn<TableViewDataEntry, String> nameColumn;
    @FXML
    private TableColumn<TableViewDataEntry, String> descriptionColumn;
    @FXML
    private TableColumn<TableViewDataEntry, Double> resultColumn;


    public void initialize() {

        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));

        tableView.getItems().addAll(getTableEntries());

    }

    private List<TableViewDataEntry> getTableEntries() {
        List<TableViewDataEntry> data = new ArrayList<>();

        List<Enrollment> enrollments = Queries.getAllInTableUsing(
                Schema.Tables.ENROLLMENT,
                Enrollment.Column.STUDENT_ID,
                StudentHomeScreen.getInstance().getStudent().id()
        );
        enrollments.forEach(enrollment -> {
            Optional<Course> courseOptional = Queries.getFromTableUsing(
                    Schema.Tables.COURSE,
                    Course.Column.ID,
                    enrollment.courseId()
            );
            courseOptional.ifPresent(course -> {
                List<Exam> examList = Queries.getAllInTableUsing(
                        Schema.Tables.EXAM,
                        Exam.Column.COURSE_ID,
                        course.id()
                );
                examList.forEach(exam -> data.add(getExamResultForExam(course, exam)));
            });
        });
        return data;
    }

    private static TableViewDataEntry getExamResultForExam(Course course, Exam exam) {
        Optional<ExamResult> examResult = Queries.getFromTableUsing(
                Schema.Tables.EXAM_RESULT,
                ExamResult.Column.EXAM_ID,
                exam.id()
        );

        return new TableViewDataEntry(
                course.name(),
                exam.name(),
                exam.description(),
                examResult.orElseThrow().grade()
        );
    }

    public static Exams getInstance() {
        return controller.getController();
    }

    @Getter
    public static class TableViewDataEntry {
        private final String course;
        private final String name;
        private final String description;
        private final double result;

        public TableViewDataEntry(String course, String name, String description, double result) {
            this.course = course;
            this.name = name;
            this.description = description;
            this.result = result;
        }
    }
}
