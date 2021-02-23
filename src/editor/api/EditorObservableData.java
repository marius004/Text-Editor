package editor.api;

public class EditorObservableData {

    private Command command;
    private String data;

    public EditorObservableData(Command command, String data) {
        this.command = command;
        this.data    = data;
    }

    public Command getCommand() {
        return this.command;
    }

    public String getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "EditorObservableData{" +
                "command=" + command +
                ", data='" + data + '\'' +
                '}';
    }
}
