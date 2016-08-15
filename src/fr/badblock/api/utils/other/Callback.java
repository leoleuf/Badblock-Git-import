package fr.badblock.api.utils.other;

public interface Callback<T> {
	public void call(Throwable error, T result);
}
