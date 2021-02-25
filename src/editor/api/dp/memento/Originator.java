package editor.api.dp.memento;

public class Originator<T> {

    private T state;

    public Originator() {}

    public void setState(T state) {
        this.state = state;
    }

    public Memento<T> createMemento() {
        return new Memento<T>(state);
    }

    public T getState() {
        return state;
    }

    public void restore(Memento<T> memento) {
        this.state = memento.getState();
    }

    public Memento<T> save() {
        return new Memento<T>(state);
    }
}