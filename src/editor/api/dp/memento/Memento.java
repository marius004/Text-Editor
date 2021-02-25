package editor.api.dp.memento;

/// https://www.dofactory.com/net/memento-design-pattern
public class Memento<T> {

    private T state;

    public Memento(T state) {
        this.state = state;
    }

    public T getState() {
        return this.state;
    }

    public void setState(T state) {
        this.state = state;
    }
}
