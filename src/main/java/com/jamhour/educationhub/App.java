package com.jamhour.educationhub;

import atlantafx.base.theme.Theme;
import com.jamhour.database.Database;
import com.jamhour.educationhub.controllers.ControllerResource;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public final class App extends Application implements AutoCloseable {

    private static final String APP_NAME = "EducationHub";

    // TODO : Fix this to contain all the dependencies that it needs
    private static final Database database = Database.getInstance();
    private static final App app = new App();
    private Stage stage;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Window window;

    public static App getInstance() {
        return app;
    }

    @Override
    public void close() throws Exception {
        database.close();
        stage.close();
    }

    @Override
    public void stop() throws Exception {
        close();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        app.stage = stage;

        setTheme(Themes.DRACULA.getTheme());
        setSceneAndShow(ControllerResource.LOGIN.getContent(), ControllerResource.LOGIN.isResizable());
    }

    public void setSceneAndShow(Parent content, boolean resizable) {

        var scene = new Scene(content);
        stage.setScene(scene);

        setWindow(stage.getOwner());
        stage.setResizable(resizable);
        stage.centerOnScreen();
        stage.setTitle(APP_NAME);
        stage.show();
    }


    public static void launch() {
        Application.launch();
    }

    public static String getAppName() {
        return APP_NAME;
    }

    public static void setTheme(Theme theme) {
        Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());
    }

}
