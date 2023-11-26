package gamemodel;

public interface Observer<T> {

    void onUpdate(T newStateRunning, T newStatePaused );
}
