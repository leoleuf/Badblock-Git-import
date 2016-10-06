package fr.badblock.bukkit.speak;

public interface Callback<T> {
	public void call(T result, Throwable error);
}
