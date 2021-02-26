package editor.ui;

import editor.ui.components.Editor;
import editor.ui.components.TopMenuBar;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class EditorScene extends Scene {

    private Stage stage;
    private TopMenuBar menuBar;
    private Editor editor;

    private void bindComponents() {
        ((VBox) this.getRoot()).getChildren().addAll(menuBar.getComponent());
        ((VBox) getRoot()).getChildren().add(editor.getComponent());
    }

    private void createUIComponents() {
        this.menuBar = new TopMenuBar();
        this.editor = new Editor(stage,this);
    }

    private void init() {
        menuBar.subscribe(editor);
    }

    public EditorScene(Parent parent, Stage stage) {
        super(parent);
        this.stage = stage;

        createUIComponents();
        bindComponents();
        init();
    }

    public EditorScene(Parent parent, double v, double v1, Stage stage) {
        super(parent, v, v1);
        this.stage = stage;

        createUIComponents();
        bindComponents();
        init();
    }

    public EditorScene(Parent parent, Paint paint, Stage stage) {
        super(parent, paint);
        this.stage = stage;

        createUIComponents();
        bindComponents();
        init();
    }

    public EditorScene(Parent parent, double v, double v1, Paint paint, Stage stage) {
        super(parent, v, v1, paint);
        this.stage = stage;

        createUIComponents();
        bindComponents();
        init();
    }

    public EditorScene(Parent parent, double v, double v1, boolean b, Stage stage) {
        super(parent, v, v1, b);
        this.stage = stage;

        createUIComponents();
        bindComponents();
        init();
    }

    public EditorScene(Parent parent, double v, double v1, boolean b, SceneAntialiasing sceneAntialiasing, Stage stage) {
        super(parent, v, v1, b, sceneAntialiasing);
        this.stage = stage;

        createUIComponents();
        bindComponents();
        init();
    }
}