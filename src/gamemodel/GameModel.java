package gamemodel;

public class GameModel extends Subject<Boolean> {
    private boolean running;
    private boolean paused;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
        notifyObservers(running, paused);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        notifyObservers(running, paused);
    }
}
