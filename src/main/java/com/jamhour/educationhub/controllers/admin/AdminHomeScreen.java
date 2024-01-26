package com.jamhour.educationhub.controllers.admin;

import com.jamhour.educationhub.controllers.ControllerResource;
import com.jamhour.educationhub.controllers.SettingsDialog;
import com.jamhour.educationhub.controllers.admin.courses_actions.CoursesActions;
import com.jamhour.educationhub.controllers.admin.student_actions.StudentActions;
import com.jamhour.educationhub.controllers.admin.teacher_actions.TeacherActions;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class AdminHomeScreen {

    @FXML
    private BorderPane contentBorderPane;

    public void initialize() {
        contentBorderPane.setCenter(ControllerResource.ADMIN_STUDENT_ACTIONS.getContent());
    }

    @FXML
    public void onStudentActionMenuClick() {
        contentBorderPane.setCenter(ControllerResource.ADMIN_STUDENT_ACTIONS.getContent());
    }

    @FXML
    public void onTeacherActionMenuClick() {
        contentBorderPane.setCenter(ControllerResource.ADMIN_TEACHER_ACTIONS.getContent());
    }

    @FXML
    public void onCoursesActionMenuClick() {
        contentBorderPane.setCenter(ControllerResource.ADMIN_COURSE_ACTIONS.getContent());
    }

    @FXML
    public void onSettingsActionMenuClick() {
        SettingsDialog.showSettingsDialog(contentBorderPane.getScene().getWindow());
    }

    @FXML
    public void onAddStudentMenuItemClick() {
        StudentActions.getInstance().addStudent();
    }

    @FXML
    public void onDeleteStudentMenuItemClick() {
        StudentActions.getInstance().deleteStudent();
    }

    @FXML
    public void onUpdateStudentMenuItemClick() {
        StudentActions.getInstance().updateStudent();
    }

    @FXML
    public void onRegisterStudentMenuItemClick() {
        StudentActions.getInstance().registerStudent();
    }

    @FXML
    public void onAddTeacherMenuItemClick() {
        TeacherActions.getInstance().addTeacher();
    }

    @FXML
    public void onDeleteTeacherMenuItemClick() {
        TeacherActions.getInstance().deleteTeacher();
    }

    @FXML
    public void onUpdateTeacherMenuItemClick() {
        TeacherActions.getInstance().updateTeacher();
    }

    @FXML
    public void onViewCoursesMenuItemClick() {
        TeacherActions.getInstance().viewCourses();
    }

    @FXML
    public void onAddCourseMenuItemClick() {
        CoursesActions.getInstance().addCourse();
    }

    @FXML
    public void onDeleteCourseMenuItemClick() {
        CoursesActions.getInstance().deleteCourse();
    }

    @FXML
    public void onUpdateCourseMenuItemClick() {
        CoursesActions.getInstance().updateCourse();
    }

}
