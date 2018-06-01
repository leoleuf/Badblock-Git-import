package fr.badblock.toenga;

import fr.badblock.api.common.utils.JsonUtils;
import fr.badblock.toenga.utils.ToengaUtils;

public class ToengaStatus
{
	public int instanceCount;
	public String lastDate;
	
	public int nextInstance()
	{
		String date = ToengaUtils.getDate();
		
		if(lastDate == null || instanceCount == -1 || !lastDate.equals(date))
		{
			lastDate = date;
			instanceCount = 0;
		}
		else
		{
			instanceCount++;
		}
		
		JsonUtils.save(Toenga.statusFile, this, true);
		return instanceCount;
	}
}
