package editor.api.dp;

public interface Observer<T> {
    void onNext(T observableData);
    void onError();
    void onCompleted();
}