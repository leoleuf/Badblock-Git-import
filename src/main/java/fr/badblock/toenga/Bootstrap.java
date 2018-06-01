package fr.badblock.toenga;

import java.io.IOException;

import fr.badblock.toenga.models.ToengaModel;

public class Bootstrap
{
	public static void main(String[] args)
	{
		ToengaModel model = Toenga.instance.modelFromName("spigot-1.8");
		//Toenga.instance.startInstance(null, "");
	
		try
		{
			Toenga.instance.startInstance(model, "production");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//System.out.println(model == null ? "nope" : model.name);
	}
}
