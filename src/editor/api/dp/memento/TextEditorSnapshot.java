package editor.api.dp.memento;

/// this is the memento
public class TextEditorSnapshot {

    private final int cursorPosition;
    private final String text;

    public TextEditorSnapshot(String text,int cursorPosition) {
        this.text = text;
        this.cursorPosition = cursorPosition;
    }

    //// these methods represent the getState method
    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public String getText() {
        return this.text;
    }

    //// NULL OBJECT PATTERN
    public static class NoTextEditorSnapshot extends TextEditorSnapshot {
        public NoTextEditorSnapshot() {
            super("", 0);
        }
    }
}
