package editor.api.dp.memento;

import java.util.ArrayList;
import java.util.List;

public class CareTaker<T> {

    private List< Memento< T > > history;
    int current;

    public CareTaker() {
        this.history = new ArrayList<>();
        this.current = -1;
    }

    private void printHistory() {
        System.out.println("Printing state...");

        for(var m : history)
            System.out.println(m.getState());

        System.out.println();
        System.out.println();
    }

    public void clearHistory() {
        this.history.clear();
    }

    public void addMemento(Memento<T> memento) {

        /// we don't wanna add the save memento twice
        if(this.history.size() == 0 || memento.getState() != history.get(current).getState()) {
            this.history.add(memento);
            current = this.history.size() - 1;
        }

        printHistory();
    }

     public Memento<T> undo() {

        if(current <= 0) {
            current = 0;
            printHistory();
            return history.get(current);
        }

        current--;

        printHistory();
        return history.get(current);
    }

    public Memento<T> redo() {

        if(current >= this.history.size() - 1) {
            current = this.history.size() - 1;
            printHistory();
            return history.get(current);
        }

        current++;
        printHistory();

        return history.get(current);
    }
}