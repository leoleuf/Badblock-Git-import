package fr.badblock.bukkit.ladder.speak;

public interface Callback<T> {
	public void call(T result, Throwable error);
}
