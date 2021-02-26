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

/*

https://refactoring.guru/design-patterns/memento#:~:text=Memento%20is%20a%20behavioral%20design,the%20details%20of%20its%20implementation.

Memento design pattern

Originator(In my case the text editor)
	=> the owner of the current state
makeSnapshot()/ save() returns a new memento containing the current state
restore(Memento m) restore the state to the state m.getState()
Snapshot which “is a” Memento
=> holds the data at a particular period of time
=> also implements getState()
CareTaker
	=> holds the history of the Originator
FIELDS: currentState = -1(Integer)
 	       List<Memento> hist;
addMemento(Memento m) {
hist.add(m);
current = hist.size() - 1;
}

getMemento(int index) => hist.get(index);


undo() // go to prev state(if available)
redo() // go the the next state(if available)
 */
