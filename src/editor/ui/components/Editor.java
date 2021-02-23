package editor.ui.components;

import editor.api.EditorObservableData;
import editor.api.FileReader;
import editor.api.dp.Observer;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;

import java.io.FileNotFoundException;

/// GOOD FOR SYNTAX HIGHLIGHTING
//https://stackoverflow.com/questions/9128535/highlighting-strings-in-javafx-textarea/53332158#53332158

public class Editor implements Observer<EditorObservableData> {

    private TextArea textArea;
    private Scene scene;
    private String copiedText = "";

    public Editor(Scene scene) {

        this.scene = scene;
        this.textArea = new TextArea();

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
               openFile(observableData);
               break;

           case COPY:
               copyText();
               break;

           case PASTE:
               pasteText();
               break;
       }
    }



    void pasteText() {
        int cursorPosition = textArea.getCaretPosition();

        var text = textArea.getText().substring(0, cursorPosition)
                + copiedText
                + textArea.getText().substring(cursorPosition);

        textArea.setText(text);
        textArea.positionCaret(cursorPosition + copiedText.length());
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
        }
    }

    void openFile(EditorObservableData observableData) {
        FileReader reader = new FileReader(observableData.getData());

        try {
            var text = reader.readFile();
            textArea.setText(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
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