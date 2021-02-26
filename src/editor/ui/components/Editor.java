package editor.ui.components;

import editor.api.dp.memento.TextEditorCareTaker;
import editor.api.dp.memento.TextEditorSnapshot;
import editor.api.dp.observer.EditorObservableData;
import editor.api.dp.observer.Observer;
import editor.api.helpers.FileReader;
import editor.api.helpers.FileSaver;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;

// https://refactoring.guru/design-patterns/memento#:~:text=Memento%20is%20a%20behavioral%20design,the%20details%20of%20its%20implementation.
/** this class is the Originator **/

public class Editor implements Observer<EditorObservableData> {

    // private int cursorPosition; => the equivalent is textArea.getCaretPosition()
    // private String text; => the equivalent is textArea.getText()

    private TextArea textArea;
    private Scene scene;
    private Stage stage;
    private String copiedText = "";
    private String filePath = "";

    private TextEditorCareTaker historyProvider; /// what do u think about the naming?))

    public Editor(Stage stage, Scene scene) {

        this.stage = stage;
        this.scene = scene;
        this.textArea = new TextArea();
        this.historyProvider = new TextEditorCareTaker();

        configureTextArea();
    }

    public void configureTextArea() {
        textArea.prefWidthProperty().bind(scene.widthProperty());
        textArea.prefHeightProperty().bind(scene.heightProperty());
        textArea.setStyle("-fx-font-size: 20px;-fx-cursor: HAND;");
        textArea.setCursor(Cursor.HAND);
    }

    public TextArea getComponent() {
        return this.textArea;
    }

    @Override
    public void onNext(EditorObservableData observableData) {
        System.out.println(observableData);

       switch(observableData.getCommand()) {

           case DELETE:
               deleteText();
               break;

           case OPEN_FILE:
               openFile();
               break;

           case CLOSE_FILE:
               closeApp();
               break;

           case COPY:
               copyText();
               break;

           case PASTE:
               pasteText();
               break;

           case SAVE:
               saveToFile();
               break;

           case UNDO:
               undo();
               break;

           case REDO:
               redo();
               break;

           default:
               System.out.println(observableData.getCommand() + " not implemented");
               break;
       }
    }

    void closeApp() {
        saveToFile();
        historyProvider.clearHistory();

        System.exit(0);
    }

    void undo() {
        var memento = historyProvider.undo();
        restore(memento);
    }

    void redo() {
        var memento = historyProvider.redo();
        restore(memento);
    }

    void saveToFile() {

        if (this.filePath == "") {

            FileChooser fileChooser = new FileChooser();
            File newFile = fileChooser.showSaveDialog(stage);

            if(textArea.getText() != historyProvider.getCurrentState().getText())
                historyProvider.addMemento(save()); //// it is like calling originator.save()

            if(newFile != null) {
                filePath = newFile.getAbsolutePath();
                updateStageTitle(newFile.getName());
            }

            return;
        }

        FileSaver saver = new FileSaver(this.filePath, this.textArea.getText());

        if(textArea.getText() != historyProvider.getCurrentState().getText())
            historyProvider.addMemento(save());

        try {
            saver.save();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void pasteText() {
        int cursorPosition = textArea.getCaretPosition();

        var text = textArea.getText().substring(0, cursorPosition)
                + copiedText
                + textArea.getText().substring(cursorPosition);

        if(text != textArea.getText()) {

            if(textArea.getText() != historyProvider.getCurrentState().getText())
                historyProvider.addMemento(save());

            textArea.setText(text);
            textArea.positionCaret(cursorPosition + copiedText.length());
        }
    }

    void copyText() {
        copiedText = textArea.getSelectedText();
        System.out.println(copiedText);
    }

    void deleteText() {
        var selectedText = textArea.getSelectedText();

        if(selectedText != "") {
            var entireText = textArea.getText();
            var res =  entireText.replace(selectedText, "");
            int caretPosition = entireText.indexOf(selectedText);

            textArea.setText(res);
            textArea.positionCaret(caretPosition);

            historyProvider.addMemento(save());
        }
    }

    void updateStageTitle(String title) {
        if(title != "" && title != null)
            this.stage.setTitle(title);
    }

    void openFile() {

        if(textArea.getText().trim() != "")
            saveToFile();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");

        File file = fileChooser.showOpenDialog(stage);

        if(file != null) {

            /// a new file => clear the careTaker's history
            this.historyProvider.clearHistory();

            this.filePath = file.toString();
            updateStageTitle(file.getName());

            try {
                FileReader reader = new FileReader(this.filePath);
                var text = reader.readFile();
                textArea.setText(text);

                if(textArea.getText() != historyProvider.getCurrentState().getText())
                    historyProvider.addMemento(save());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError() {}

    @Override
    public void onCompleted() {}

    /// memento design pattern stuff below
    private TextEditorSnapshot save() {
        return new TextEditorSnapshot(textArea.getText(), textArea.getCaretPosition());
    }

    private void restore(TextEditorSnapshot snapshot) {
        textArea.setText(snapshot.getText());
        textArea.positionCaret(snapshot.getCursorPosition());
    }
}