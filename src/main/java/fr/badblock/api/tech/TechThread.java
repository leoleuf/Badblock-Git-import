package fr.badblock.api.tech;

import fr.badblock.api.BadBlockAPI;

import java.util.Queue;

public abstract class TechThread<T> extends Thread{

    private Queue<T> queue;

    public TechThread(String technologyName, Queue<T> queue, int id)
    {
        super("BadBlockAPI/" + technologyName + "/" + id);
        setQueue(queue);
        start();
    }

    @Override
    public void run()
    {
        synchronized (this)
        {
            while (isServiceAlive())
            {
                while (!queue.isEmpty())
                {
                    try
                    {
                        work(queue.poll());
                    }
                    catch (Exception exception)
                    {
                        BadBlockAPI.getPluginInstance().getLogger().info(getErrorMessage());
                        exception.printStackTrace();
                    }
                }
                laze();
            }
        }
    }

    public abstract String  getErrorMessage();
    public abstract boolean isServiceAlive();
    public abstract void	work(T data) throws Exception;

    public boolean canHandlePacket()
    {
        return isAlive() && getState().equals(State.WAITING);
    }

    public void stirHimself()
    {
        synchronized (this)
        {
            this.notify();
        }
    }

    public void laze()
    {
        synchronized (this)
        {
            try
            {
                this.wait();
            }
            catch (InterruptedException exception)
            {
                exception.printStackTrace();
            }
        }
    }

    public void setQueue(Queue<T> queue) {
        this.queue = queue;
    }
}

