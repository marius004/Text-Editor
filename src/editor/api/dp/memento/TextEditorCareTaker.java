package editor.api.dp.memento;

import java.util.ArrayList;
import java.util.List;

/// this is the history class(aka CareTaker) from within the memento design pattern
public class TextEditorCareTaker {

    private final List<TextEditorSnapshot> history;
    private int current;

    public TextEditorCareTaker() {
        this.history = new ArrayList<>();
        this.current = -1;
    }

    public void addMemento(TextEditorSnapshot memento) {
        this.history.add(memento);
        current = this.history.size() - 1;
    }

    public TextEditorSnapshot undo() {

        System.out.println("UNDO");
        System.out.println("old: " + current);

        if(current <= 0) {
            current = 0;
            System.out.println("new: " + current);
            return getMemento(current);
        }

        current--;
        System.out.println("new: " + current);
        return getMemento(current);
    }

    public TextEditorSnapshot redo() {

        System.out.println("REDO");
        System.out.println("old: " + current);

        if(current >= history.size() - 1) {
            current = history.size() - 1;
            System.out.println("new: " + current);
            return getMemento(current);
        }

        current++;
        System.out.println("new: " + current);

        return getMemento(current);
    }

    public TextEditorSnapshot getMemento(int index) {
        return this.history.get(index);
    }

    public void clearHistory() {
        history.clear();
    }

    public TextEditorSnapshot getCurrentState() {
        return current >= 0 ? history.get(current) : new TextEditorSnapshot.NoTextEditorSnapshot();
    }
}
