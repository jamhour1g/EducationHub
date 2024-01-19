package com.jamhour.educationhub.controllers.admin;

import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class AdminHomeScreen {


    @FXML
    private Tab studentActionsTab;
    @FXML
    private Tab teacherActionsTab;
    @FXML
    private Tab courseActionsTab;


    public void initialize() {
        studentActionsTab.setContent(ControllerResource.ADMIN_STUDENT_ACTIONS.getContent());
        teacherActionsTab.setContent(ControllerResource.ADMIN_TEACHER_ACTIONS.getContent());
        courseActionsTab.setContent(ControllerResource.ADMIN_COURSE_ACTIONS.getContent());
    }

}
