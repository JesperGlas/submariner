package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private double WINDOW_WIDTH = 1200d;
    private double WINDOW_HEIGHT = 800d;

    private Scene startMenuScene;
    private Scene gameScene;

    private void initWindow() throws Exception {
        Parent menuRoot = FXMLLoader.load(getClass().getResource("/views/mainMenu.fxml"));
        startMenuScene = new Scene(menuRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
        startMenuScene.getStylesheets().addAll(getClass().getResource("/css/style.css").toExternalForm());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initWindow();

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(startMenuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
