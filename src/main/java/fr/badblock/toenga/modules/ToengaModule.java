package fr.badblock.toenga.modules;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

import fr.badblock.toenga.instance.ToengaInstance;

public abstract class ToengaModule
{
	public static Map<String, ToengaModule> modules()
	{
		Map<String, ToengaModule> modules = new HashMap<>();

		modules.put("choosePort", new ModuleChoosePort());
		modules.put("autoRemove", new ModuleAutoRemove());
		
		return modules;
	}
	
	public abstract void apply(ToengaInstance instance, ModuleState state, JsonElement[] parameters);
}
