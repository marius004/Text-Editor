package editor.api.dp.observer;

public interface Observer<T> {
    void onNext(T observableData);
    void onError();
    void onCompleted();
}