package editor.ui.components;

import editor.api.Command;
import editor.api.EditorObservableData;
import editor.api.dp.Disposable;
import editor.api.dp.Observable;
import editor.api.dp.Observer;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TopMenuBar implements Observable<EditorObservableData> {

    private MenuBar menuBar;
    private List< Observer < EditorObservableData > > subscribers;
    private Stage stage;

    public TopMenuBar(Stage stage) {
        this.stage = stage;
        this.menuBar     = new MenuBar();
        this.subscribers = new ArrayList<>();
        populateMenuBar();
    }

    private void populateMenuBar() {
        Menu fileMenu = createFileMenu();
        Menu menuEdit = createEditMenu();
        Menu preferencesMenu = createPreferencesMenu();

        menuBar.getMenus().addAll(fileMenu, menuEdit, preferencesMenu);
    }

    private Menu createFileMenu() {

        Menu fileMenu = new Menu("File");

        MenuItem  newItem  = new MenuItem("New");

        newItem.setOnAction(actionEvent -> {
            EditorObservableData data = new EditorObservableData(Command.NEW_FILE, "the file path should be here");
            Notify(data);
        });

        MenuItem openItem = new MenuItem("Open");

        openItem.setOnAction(actionEvent -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");

            File file = fileChooser.showOpenDialog(stage);

            if(file != null) { //if the user selected a file to open
                EditorObservableData data = new EditorObservableData(Command.OPEN_FILE, file.toString());
                Notify(data);
            }

        });

        MenuItem closeItem = new MenuItem("Close");

        MenuItem saveItem = new MenuItem("Save");

        saveItem.setOnAction(event -> {
            System.out.println("Saving stuff");
        });

        closeItem.setOnAction(actionEvent -> {
            EditorObservableData data = new EditorObservableData(Command.CLOSE_FILE, "");
            Notify(data);

            System.exit(0);
        });

        fileMenu.getItems().addAll(newItem, openItem, closeItem,saveItem);

        return fileMenu;
    }

    private Menu createEditMenu() {

        Menu editMenu = new Menu("Edit");

        MenuItem undoItem   = new MenuItem("Undo");
        MenuItem redoItem   = new MenuItem("Redo");

        MenuItem copyItem   = new MenuItem("Copy");

        copyItem.setOnAction(actionEvent -> {
            var data = new EditorObservableData(Command.COPY, "");
            Notify(data);
        });

        MenuItem pasteItem  = new MenuItem("Paste");

        pasteItem.setOnAction(event -> {
            EditorObservableData data = new EditorObservableData(Command.PASTE, "");
            Notify(data);
        });

        MenuItem deleteItem = new MenuItem("Delete");

        deleteItem.setOnAction(event -> {
            EditorObservableData data = new EditorObservableData(Command.DELETE, "");
            Notify(data);
        });

        editMenu.getItems().addAll(undoItem, redoItem, copyItem, deleteItem, pasteItem);

        return editMenu;
    }

    private Menu createPreferencesMenu() {
        Menu preferencesMenu = new Menu("Preferences");

        ///TODO add action listeners
        return preferencesMenu;
    }

    public MenuBar getComponent() {
        return this.menuBar;
    }

    @Override
    public Disposable subscribe(Observer observer) {
        this.subscribers.add(observer);
        return new Unsubscriber(subscribers, observer);
    }

    private void Notify(EditorObservableData data) {
        for(var subscriber : subscribers)
            subscriber.onNext(data);
    }

    private class Unsubscriber implements Disposable {

        private List<Observer<EditorObservableData>> _observers;
        private Observer _observer;

        private Unsubscriber(List<Observer<EditorObservableData>> observers, Observer<EditorObservableData> observer) {
            this._observers = observers;
            this._observer  = observer;
        }

        @Override
        public void dispose() {
            if(_observers.contains(_observer))
                _observers.remove(_observer);
        }
    }

}