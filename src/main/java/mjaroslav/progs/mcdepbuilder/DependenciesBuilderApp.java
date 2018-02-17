package mjaroslav.progs.mcdepbuilder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mjaroslav.progs.mcdepbuilder.DependenciesBuilder.DependenceInfo;

public class DependenciesBuilderApp extends Application {
    private static DependenciesBuilder builder = new DependenciesBuilder();

    private static DependenceInfo currentDep = new DependenceInfo("example");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dependencies Builder. WORK IN PROGRESS");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.setResizable(false);
        Pane root = new Pane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String... args) {
        launch(args);
    }
}
