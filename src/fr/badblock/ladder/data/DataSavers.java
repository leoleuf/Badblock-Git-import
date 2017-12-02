package fr.badblock.ladder.data;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import com.google.common.collect.Queues;
import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;

public class DataSavers {
	public static List<Thread> threads = new ArrayList<>();
	public static Queue<Val> toSave = Queues.newLinkedBlockingDeque();
	public static boolean allow = true;

	static {
		for (int i = 0; i < 128; i++)
		{
			Thread thread = new Thread()
			{
				@Override
				public void run()
				{
					synchronized (this)
					{
						while(true) {
							while(!toSave.isEmpty()){
								try {
									Val handler = toSave.poll();
									if(handler.handler.reading.get()){
										System.out.println("Currently reading " + handler.handler.getKey() + ", we will not saving that yet");
										toSave.add(handler);
										continue;
									}

									handler.handler.saveSync(handler.object, handler.update);
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
			};
			thread.start();
			threads.add(thread);
		}
	}

	public static void save(LadderDataHandler handler, JsonObject data, boolean update){
		if(!allow)
			return;
		if (!handler.loaded)
		{
			System.out.println("[OLAH] Tying to save but not loaded.");
			return;
		}
		toSave.add(new Val(handler, data, update));
		Optional<Thread> th = threads.parallelStream().filter(thread -> thread.isAlive() && thread.getState().equals(State.WAITING)).findAny();
		if (th != null && th.isPresent())
		{
			Thread thread = th.get();
			if (thread == null)
			{
				return;
			}
			synchronized (thread) {
				thread.notify();
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
