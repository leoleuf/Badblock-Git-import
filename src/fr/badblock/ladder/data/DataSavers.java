package fr.badblock.ladder.data;

import java.util.Queue;

import com.google.common.collect.Queues;
import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;

public class DataSavers extends Thread {
	public static Thread thread = new Thread();
	public static Queue<Val> toSave = Queues.newLinkedBlockingDeque();
	public static boolean allow = true;

	static {
		thread = new DataSavers();
		thread.start();
	}

	public static void save(LadderDataHandler handler, JsonObject data, boolean update){
		if(!allow)
			return;
		toSave.add(new Val(handler, data, update));
		synchronized (thread) {
			thread.notify();
		}
	}

	@Override
	public void run(){
		synchronized (this) {
			while(true){
				while(!toSave.isEmpty()){
					try {
						Val handler = toSave.poll();
						if(handler.handler.reading.get()){
							System.out.println("Currently reading " + handler.handler.getKey() + ", we will not saving that yet");
							toSave.add(handler);
							continue;
						}

						if (handler.handler.getKey().startsWith("89.170.77.") || handler.handler.getKey().startsWith("Akise") || handler.handler.getKey().startsWith("akise"))
							System.out.println("Saving " + handler.handler.getKey() + " (" + handler.handler.getData() + ")...");
						handler.handler.saveSync(handler.object, handler.update);
						if (handler.handler.getKey().startsWith("89.170.77.") || handler.handler.getKey().startsWith("Akise") || handler.handler.getKey().startsWith("akise"))
							System.out.println("Saved " + handler.handler.getKey() + "  (" + handler.handler.getData() + ")!");
					} catch(Throwable t){}
				}
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@AllArgsConstructor
	static class Val {
		LadderDataHandler handler;
		JsonObject object;
		boolean update;
	}
}
