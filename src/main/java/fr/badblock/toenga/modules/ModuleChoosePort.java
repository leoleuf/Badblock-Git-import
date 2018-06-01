package fr.badblock.toenga.modules;

import java.io.IOException;
import java.net.ServerSocket;

import com.google.gson.JsonElement;

import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.instance.ToengaInstance;

public class ModuleChoosePort extends ToengaModule
{
	@Override
	public void apply(ToengaInstance instance, ModuleState state, JsonElement[] parameters)
	{
		if(state == ModuleState.BEFORE_START)
		{
	        int port = 0;
	        
	        try
	        {
	            ServerSocket server = new ServerSocket(0);
	            port = server.getLocalPort();
	            server.close();
	        }
	        catch (IOException e)
	        {
	            throw new IllegalStateException("Can't find a free port", e);
	        }
			
			Toenga.debug("Allocate port to instance", instance, port);
			instance.getData().put("port", port);
		}
	}

}
