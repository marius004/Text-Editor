package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import editor.ui.EditorScene;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        var editorScene = new EditorScene(new VBox(), 700, 600, stage);

        configureStage(stage, editorScene);
        stage.show();
    }

    public void configureStage(Stage stage, Scene editorScene) {
        stage.setTitle("Text editor");
        stage.setScene(editorScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
