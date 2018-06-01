package fr.badblock.toenga.models;

import com.google.gson.JsonElement;

import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.instance.locale.LocalInstance;
import fr.badblock.toenga.modules.ModuleState;
import fr.badblock.toenga.modules.ToengaModule;

public class ToengaModelModule
{
	public String module;
	public JsonElement[] params;
	
	public void apply(LocalInstance instance, ModuleState state)
	{
		ToengaModule module = Toenga.instance.moduleFromName(this.module);
	
		if(module == null)
		{
			Toenga.debug("Unknow module", module);
		}
		else
		{
			module.apply(instance, state, params);
		}
	}
}
