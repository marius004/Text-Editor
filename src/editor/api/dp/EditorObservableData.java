package editor.api.dp;

import editor.api.Command;

public class EditorObservableData {

    private Command command;

    public EditorObservableData(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return this.command;
    }

    @Override
    public String toString() {
        return "EditorObservableData{" +
                "command=" + command +
                '}';
    }
}
