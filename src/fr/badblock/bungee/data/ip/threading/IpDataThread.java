package fr.badblock.bungee.data.ip.threading;

import java.util.ConcurrentModificationException;
import java.util.Queue;

import com.google.common.collect.Queues;

import fr.badblock.bungee.data.ip.BadIpData;
import fr.badblock.bungee.data.ip.utils.IpDataUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data@EqualsAndHashCode(callSuper=false) public class IpDataThread extends Thread {

	public Queue<BadIpData> queue = Queues.newLinkedBlockingDeque();

	public IpDataThread(int id) {
		this.setName("DataThread/" + id);
		this.start();
	}

	@Override
	public void run() {
		synchronized (this) {
			while (true) {
				while (!queue.isEmpty()) {
					BadIpData badIpData = queue.poll();
					if (badIpData != null) {
						IpDataUtils.saveData(badIpData);
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

	public void addIpData(BadIpData ipData) {
		if (queue.contains(ipData)) {
			try {
				throw new ConcurrentModificationException("Concurrent save " + ipData.getIp());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		queue.add(ipData);
		if (this.getState().equals(State.WAITING)) {
			synchronized (this) {
				this.notify();
			}
		}
	}

}
