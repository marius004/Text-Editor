package editor.ui.components;

import editor.api.dp.memento.CareTaker;
import editor.api.dp.memento.Originator;
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

/// GOOD FOR SYNTAX HIGHLIGHTING
//https://stackoverflow.com/questions/9128535/highlighting-strings-in-javafx-textarea/53332158#53332158

public class Editor implements Observer<EditorObservableData> {

    private TextArea textArea;
    private Scene scene;
    private Stage stage;
    private String copiedText = "";
    private String filePath = "";
    private Originator<String> originator;
    private CareTaker<String> careTaker;

    public Editor(Stage stage, Scene scene) {

        this.stage = stage;
        this.scene = scene;
        this.textArea = new TextArea();

        this.originator = new Originator<String>();
        this.careTaker = new CareTaker<String>();

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
        careTaker.clearHistory();

        System.exit(0);
    }

    void undo() {
        var memento = careTaker.undo();
        textArea.setText(memento.getState());
    }

    void redo() {
        var memento = careTaker.redo();
        textArea.setText(memento.getState());
    }

    void saveToFile() {

        if (this.filePath == "") {

            FileChooser fileChooser = new FileChooser();
            File newFile = fileChooser.showSaveDialog(stage);

            ///update the careTaker
            originator.setState(textArea.getText());
            careTaker.addMemento(originator.save());

            if(newFile != null) {
                filePath = newFile.getAbsolutePath();
                updateStageTitle(newFile.getName());
            }

            return;
        }

        FileSaver saver = new FileSaver(this.filePath, this.textArea.getText());

        //// update the careTaker
        originator.setState(this.textArea.getText());
        careTaker.addMemento(originator.save());

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

            /// update the careTaker
            originator.setState(text);
            careTaker.addMemento(originator.save());

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

            //update the careTaker
            originator.setState(res);
            careTaker.addMemento(originator.save());

            textArea.setText(res);
            textArea.positionCaret(caretPosition);
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
            this.careTaker.clearHistory();

            this.filePath = file.toString();
            updateStageTitle(file.getName());

            try {
                FileReader reader = new FileReader(this.filePath);
                var text = reader.readFile();
                textArea.setText(text);

                originator.setState(text);
                careTaker.addMemento(originator.save());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError() {
        System.out.println("Some unexpected exception occurred");
    }

    @Override
    public void onCompleted() {
        System.out.println("I am done being an observer");
    }
}