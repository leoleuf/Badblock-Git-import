package fr.badblock.ladder.data;

import java.util.Queue;

import com.google.common.collect.Queues;

public class DataSavers extends Thread {
	private static Queue<LadderDataHandler> toSave = Queues.newLinkedBlockingDeque();

	static {
		new DataSavers().start();
	}
	
	public static void save(LadderDataHandler handler){
		toSave.add(handler);
	}
	
	@Override
	public void run(){
		while(true){
			LadderDataHandler handler = null;
			
			while((handler = toSave.poll()) != null){
				try {
					handler.saveSync();
				} catch(Throwable t){}
			}
		}
	}
}
