package com.jamhour.educationhub.controllers.student;

import com.jamhour.data.Course;
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
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDialog {

    private static final ControllerResource CONTROLLER_RESOURCE = ControllerResource.COURSES_DIALOG;

    @FXML
    private TableView<TableViewDataEntry> examTableView;
    @FXML
    private TableColumn<TableViewDataEntry, String> nameColumn;
    @FXML
    private TableColumn<TableViewDataEntry, String> timeColumn;
    @FXML
    private TableColumn<TableViewDataEntry, String> descriptionColumn;
    @FXML
    private TableColumn<TableViewDataEntry, String> resultColumn;

    @Setter
    private Course course;

    public void initialize() {

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));

        if (course == null) {
            return;
        }
        
        examTableView.getItems().clear();
        examTableView.getItems().addAll(getTableEntries());
    }

    private List<TableViewDataEntry> getTableEntries() {
        List<TableViewDataEntry> data = new ArrayList<>();

        List<Exam> examsForCourse = Queries.getAllInTableUsing(
                Schema.Tables.EXAM,
                Exam.Column.COURSE_ID,
                course.id()
        );
        examsForCourse.forEach(exam -> data.add(createTableEntry(exam)));

        return data;
    }

    private static TableViewDataEntry createTableEntry(Exam exam) {
        Optional<ExamResult> examFromDatabase = Queries.getFromTableUsing(
                Schema.Tables.EXAM_RESULT,
                ExamResult.Column.EXAM_ID,
                exam.id()
        );

        return new TableViewDataEntry(
                exam.name(),
                exam.examDateTime(),
                exam.description(),
                examFromDatabase.orElseThrow().grade()
        );
    }

    public static CourseDialog getInstance() {
        return CONTROLLER_RESOURCE.getController();
    }

    @Getter
    public static class TableViewDataEntry {
        private final String name;
        private final String time;
        private final String description;
        private final double result;

        public TableViewDataEntry(String name, LocalDateTime time, String description, double result) {
            this.name = name;
            this.time = time.format(
                    DateTimeFormatter.ofLocalizedDateTime(
                            FormatStyle.MEDIUM
                    )
            );
            this.description = description;
            this.result = result;
        }

    }
}
