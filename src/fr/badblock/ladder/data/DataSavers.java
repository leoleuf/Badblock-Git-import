package fr.badblock.ladder.data;

import java.util.Queue;

import com.google.common.collect.Queues;
import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;

public class DataSavers extends Thread {
	public static Thread data = new Thread();
	public static Queue<Val> toSave = Queues.newLinkedBlockingDeque();
	public static boolean allow = true;
	
	static {
		new DataSavers().start();
	}
	
	public static void save(LadderDataHandler handler, JsonObject data, boolean update){
		if(!allow)
			return;
		
		toSave.add(new Val(handler, data, update));
	}
	
	@Override
	public void run(){
		while(true){
			Val handler = null;
			
			while((handler = toSave.poll()) != null){
				try {
					if(handler.handler.reading.get()){
						toSave.add(handler);
						continue;
					}
					
					handler.handler.saveSync(handler.object, handler.update);
				} catch(Throwable t){}
			}
			
			try {
				Thread.sleep(50L);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
