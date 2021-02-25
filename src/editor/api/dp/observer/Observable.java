package editor.api.dp.observer;

import editor.api.dp.Disposable;

public interface Observable<T> {
    Disposable subscribe(Observer observer);
}
