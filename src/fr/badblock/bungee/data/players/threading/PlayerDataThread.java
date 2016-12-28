package fr.badblock.bungee.data.players.threading;

import java.util.ConcurrentModificationException;
import java.util.Queue;

import com.google.common.collect.Queues;

import fr.badblock.bungee.data.players.BadOfflinePlayer;
import fr.badblock.bungee.data.players.utils.PlayerDataUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data@EqualsAndHashCode(callSuper=false) public class PlayerDataThread extends Thread {

	public Queue<BadOfflinePlayer> queue = Queues.newLinkedBlockingDeque();

	public PlayerDataThread(int id) {
		this.setName("DataThread/" + id);
		this.start();
	}

	@Override
	public void run() {
		synchronized (this) {
			while (true) {
				while (!queue.isEmpty()) {
					BadOfflinePlayer badPlayer = queue.poll();
					if (badPlayer != null) {
						PlayerDataUtils.saveData(badPlayer);
					}
				}
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void addPlayer(BadOfflinePlayer player) {
		if (queue.contains(player)) {
			try {
				throw new ConcurrentModificationException("Concurrent save " + player.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		queue.add(player);
		if (this.getState().equals(State.WAITING)) {
			synchronized (this) {
				this.notify();
			}
		}
	}

}
