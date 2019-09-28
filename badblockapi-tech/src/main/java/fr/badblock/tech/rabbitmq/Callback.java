package fr.badblock.tech.rabbitmq;

public abstract class Callback<T>
{

    public abstract void done(T result, Throwable error);

}