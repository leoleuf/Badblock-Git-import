package fr.badblock.toenga.modules;

import com.google.gson.JsonElement;

import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.instance.ToengaInstance;

public class ModuleAutoRemove extends ToengaModule
{
	@Override
	public void apply(ToengaInstance instance, ModuleState state, JsonElement[] parameters)
	{
		if(state == ModuleState.AFTER_STOP || state == ModuleState.RECOVER)
		{
			Toenga.log("AutoRemove: destroy '" + instance.getId() + "'");
			instance.destroy();
		}
	}
}
