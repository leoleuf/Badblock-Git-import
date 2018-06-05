package fr.badblock.toenga.sync.toenga;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.toenga.Toenga;

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
