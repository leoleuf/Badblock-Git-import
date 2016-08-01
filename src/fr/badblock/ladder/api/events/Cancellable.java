package fr.badblock.ladder.api.events;

public interface Cancellable {
	public void setCancelled(boolean cancelled);
	public boolean isCancelled();
}
