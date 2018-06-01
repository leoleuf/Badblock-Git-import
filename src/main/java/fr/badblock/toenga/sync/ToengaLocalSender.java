package fr.badblock.toenga.sync;

import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.utils.TimeUtils;

public class ToengaLocalSender extends Thread
{

	private long waitTime;

	public ToengaLocalSender()
	{
		super("toenga-localSender");
		waitTime = Math.max(1, Toenga.instance.getConfiguration().getKeepAliveTime());
	}

	@Override
	public void run()
	{
		while (true)
		{
			ToengaSyncManager.getInstance().keepAlive();
			TimeUtils.sleepInSeconds(waitTime);
		}
	}

}
