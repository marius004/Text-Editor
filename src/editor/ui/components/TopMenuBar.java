package editor.ui.components;

import editor.api.Command;

import editor.api.dp.Disposable;
import editor.api.dp.EditorObservableData;
import editor.api.dp.Observable;
import editor.api.dp.Observer;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class TopMenuBar implements Observable<EditorObservableData> {

    private MenuBar menuBar;
    private List< Observer < EditorObservableData > > subscribers;

    public TopMenuBar() {
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
            EditorObservableData data = new EditorObservableData(Command.NEW_FILE);
            Notify(data);
        });

        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(actionEvent -> {
              EditorObservableData data = new EditorObservableData(Command.OPEN_FILE);
              Notify(data);
        });

        MenuItem closeItem = new MenuItem("Close");
        closeItem.setOnAction(actionEvent -> {
            EditorObservableData data = new EditorObservableData(Command.CLOSE_FILE);
            Notify(data);
        });

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(event -> {
            EditorObservableData data = new EditorObservableData(Command.SAVE);
            Notify(data);
        });

        fileMenu.getItems().addAll(newItem, openItem, closeItem,saveItem);

        return fileMenu;
    }

    private Menu createEditMenu() {

        Menu editMenu = new Menu("Edit");

        MenuItem undoItem   = new MenuItem("Undo");
        undoItem.setOnAction(actionEvent -> {
            var data = new EditorObservableData(Command.UNDO);
            Notify(data);
        });

        MenuItem redoItem   = new MenuItem("Redo");
        redoItem.setOnAction(actionEvent -> {
            var data = new EditorObservableData(Command.REDO);
            Notify(data);
        });

        MenuItem copyItem   = new MenuItem("Copy");
        copyItem.setOnAction(actionEvent -> {
            var data = new EditorObservableData(Command.COPY);
            Notify(data);
        });

        MenuItem pasteItem  = new MenuItem("Paste");
        pasteItem.setOnAction(event -> {
            EditorObservableData data = new EditorObservableData(Command.PASTE);
            Notify(data);
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            EditorObservableData data = new EditorObservableData(Command.DELETE);
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