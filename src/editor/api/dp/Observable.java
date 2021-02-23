package editor.api.dp;

public interface Observable<T> {
    Disposable subscribe(Observer observer);
}
