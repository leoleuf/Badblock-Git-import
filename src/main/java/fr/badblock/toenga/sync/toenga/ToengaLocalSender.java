package fr.badblock.toenga.sync.toenga;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.toenga.Toenga;

public class ToengaLocalSender extends Thread
{

	private long waitTime;

	public ToengaLocalSender(Toenga toenga)
	{
		super("toenga-localSender");
		waitTime = Math.max(1, toenga.getConfiguration().getKeepAliveTime());
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
