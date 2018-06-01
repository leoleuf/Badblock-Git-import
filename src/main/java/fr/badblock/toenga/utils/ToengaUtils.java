package fr.badblock.toenga.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.instance.ToengaInstance;
import fr.badblock.toenga.models.ToengaModel;

public abstract class ToengaUtils
{
	private static final DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
	private static final String base32 = "0123456789abcdefghijklmnopqrstu";

	public static String idToStr(int id)
	{
		String base = "";

		for(int i = 0; id > 0 || i < 3; i++)
		{
			base = base32.charAt(id & 31) + base;
			id >>= 5;
		}
		
		return base;
	}
	
	public static String createId(ToengaModel model)
	{
		StringBuilder builder = new StringBuilder();

		builder.append(Toenga.instance.getHostname());
		builder.append('_');
		builder.append(getDate());
		builder.append('_');
		builder.append(model.name);
		builder.append('_');
		builder.append( idToStr(Toenga.instance.nextInstanceId()) );

		return builder.toString();
	}
	
    public static String formatToengaStr(String str, ToengaInstance process)
    {
        final StringBuilder builder = new StringBuilder();
        int start = 0;
        int startKey = -1;

        for (int i = 0; i < str.length(); ++i)
        {
            if (startKey != -1 && str.charAt(i) == '}')
            {
                final String key = str.substring(startKey + 1, i);
                Object value = process.getData().get(key);

                if (value != null)
                {
                    builder.append(str, start, startKey);
                    builder.append(value);
                    start = i + 1;
                }
                else
                {
                	Toenga.debug("Unknow data", key, process.getId());
                }
                
                startKey = -1;
            }
            else if (str.charAt(i) == '{')
            {
                startKey = i;
            }
        }

        if (start < str.length())
        {
            builder.append(str, start, str.length());
        }

        return builder.toString();
    }
    
    public static String getDate()
    {
    	return dateFormat.format(new Date());
    }
}
