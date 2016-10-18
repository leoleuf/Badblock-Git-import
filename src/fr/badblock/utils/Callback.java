package fr.badblock.utils;

public interface Callback<T> {
	public void done(T result, Throwable error);
}
