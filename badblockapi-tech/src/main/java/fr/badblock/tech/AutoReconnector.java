package fr.badblock.tech;

import java.util.Timer;
import java.util.TimerTask;

public abstract class AutoReconnector extends Service {

    private TimerTask task;

    public AutoReconnector(String name, Settings settings) {
        super(name, settings);
        task = run();
        new Timer().schedule(task, 1000, 1000);
    }

    public abstract boolean isConnected();

    public abstract void reconnect();

    private TimerTask run() {
        return new TimerTask() {
            @Override
            public void run() {
                reconnect();
            }
        };
    }

    protected TimerTask getTask(){
        return task;
    }

}
