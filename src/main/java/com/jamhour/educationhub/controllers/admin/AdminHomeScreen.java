package com.jamhour.educationhub.controllers.admin;

import com.jamhour.educationhub.controllers.ControllerResource;
import com.jamhour.educationhub.controllers.SettingsDialog;
import com.jamhour.educationhub.controllers.admin.courses_actions.AddCourseDialog;
import com.jamhour.educationhub.controllers.admin.courses_actions.DeleteCourseDialog;
import com.jamhour.educationhub.controllers.admin.courses_actions.UpdateCourse;
import com.jamhour.educationhub.controllers.admin.student_actions.AddStudentDialog;
import com.jamhour.educationhub.controllers.admin.student_actions.DeleteStudentDialog;
import com.jamhour.educationhub.controllers.admin.student_actions.RegisterStudentDialog;
import com.jamhour.educationhub.controllers.admin.student_actions.UpdateStudent;
import com.jamhour.educationhub.controllers.admin.teacher_actions.AddTeacherDialog;
import com.jamhour.educationhub.controllers.admin.teacher_actions.DeleteTeacherDialog;
import com.jamhour.educationhub.controllers.admin.teacher_actions.UpdateTeacher;
import com.jamhour.educationhub.controllers.admin.teacher_actions.ViewCoursesForTeacherDialog;
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
        AddStudentDialog.showAddStudentDialog(contentBorderPane.getScene().getWindow());
    }

    @FXML
    public void onDeleteStudentMenuItemClick() {
        DeleteStudentDialog.showDeleteStudentDialog(contentBorderPane.getScene().getWindow());
    }

    @FXML
    public void onUpdateStudentMenuItemClick() {
        UpdateStudent.updateStudentInfoInDatabase(contentBorderPane.getScene().getWindow());
    }

    @FXML
    public void onRegisterStudentMenuItemClick() {
        RegisterStudentDialog.showRegisterStudentDialog(contentBorderPane.getScene().getWindow());
    }

    @FXML
    public void onAddTeacherMenuItemClick() {
        AddTeacherDialog.showAddTeacherDialog(contentBorderPane.getScene().getWindow());
    }

    @FXML
    public void onDeleteTeacherMenuItemClick() {
        DeleteTeacherDialog.showDeleteTeacherDialog(contentBorderPane.getScene().getWindow());
    }

    @FXML
    public void onUpdateTeacherMenuItemClick() {
        UpdateTeacher.updateTeacherInfoInDatabase(contentBorderPane.getScene().getWindow());
    }

    @FXML
    public void onViewCoursesMenuItemClick() {
        ViewCoursesForTeacherDialog.showCoursesForTeacherDialog(contentBorderPane.getScene().getWindow());
    }

    @FXML
    public void onAddCourseMenuItemClick() {
        AddCourseDialog.showAddCourseDialog(contentBorderPane.getScene().getWindow());
    }

    @FXML
    public void onDeleteCourseMenuItemClick() {
        DeleteCourseDialog.showDeleteCourseDialog(contentBorderPane.getScene().getWindow());
    }

    @FXML
    public void onUpdateCourseMenuItemClick() {
        UpdateCourse.updateCourseInfoInDatabase(contentBorderPane.getScene().getWindow());
    }

}
