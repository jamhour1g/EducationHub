package com.jamhour.educationhub.controllers.student;

import com.jamhour.data.Course;
import com.jamhour.data.Enrollment;
import com.jamhour.data.Teacher;
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

public class Courses {
    private static final ControllerResource controller = ControllerResource.STUDENT_COURSES;
    @FXML
    private TableView<CoursesTableDataEntry> coursesTableView;
    @FXML
    private TableColumn<CoursesTableDataEntry, String> teacherNameColumn;
    @FXML
    private TableColumn<CoursesTableDataEntry, String> courseColumn;
    @FXML
    private TableColumn<CoursesTableDataEntry, String> courseIdColumn;

    public void initialize() {

        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        coursesTableView.getItems().addAll(getTableEntries());
    }

    private static List<CoursesTableDataEntry> getTableEntries() {
        List<CoursesTableDataEntry> data = new ArrayList<>();

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
                Optional<Teacher> teacherOptional = Queries.getFromTableUsing(
                        Schema.Tables.TEACHER,
                        Teacher.Column.ID,
                        course.teacherId()
                );

                teacherOptional.ifPresent(teacher ->
                        data.add(
                                new CoursesTableDataEntry(
                                        course.name(),
                                        teacher.name(),
                                        course.id()
                                )
                        )
                );

            });
        });
        return data;
    }

    public static Courses getInstance() {
        return controller.getController();
    }

    @Getter
    public static class CoursesTableDataEntry {
        private final String courseName;
        private final String teacherName;
        private final int courseId;

        public CoursesTableDataEntry(String courseName, String teacherName, int courseId) {
            this.courseName = courseName;
            this.teacherName = teacherName;
            this.courseId = courseId;
        }
    }
}
