package fr.badblock.api.utils;


public abstract class Callback<T>
{

    public abstract void done(T result, Throwable error);

}
