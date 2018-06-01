package fr.badblock.toenga.models;

import java.io.File;
import java.util.Map;

import fr.badblock.api.common.utils.FileUtils;
import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.instance.locale.LocalInstance;
import fr.badblock.toenga.modules.ModuleState;

public class ToengaModel
{
	public String name;
	public String command;
	public String[] parents;
	
	public ToengaModelFile[] create;
	public String[] update_remove;
	public ToengaModelFile[] update_copy;

	public ToengaModelModule[] modules;

	/**
	 * Can this model been started ?
	 */
	public boolean startable;	
	public Map<String, Integer> autoOpen;
	
	public String getCommand()
	{
		if(this.command != null)
			return this.command;
		
		for(String parent : parents)
		{
			ToengaModel model = Toenga.instance.modelFromName(parent);
			
			if(model == null)
			{
				Toenga.debug(this.name, "Parent does not exists", parent);
			}
			else
			{
				String command = model.getCommand();
				
				if(command != null)
				{
					return command;
				}
			}
		}
		
		return null;
	}
	
	public Map<String,Object> getData()
	{
		Map<String, Object> result = null;
		
		for(String parent : parents)
		{
			ToengaModel model = Toenga.instance.modelFromName(parent);
			
			if(model == null)
			{
				Toenga.debug(this.name, "Parent does not exists", parent);
			}
			else
			{
				Map<String, Object> par = model.getData();
				
				if(result == null)
					result = par;
				else
					result.putAll(par);
			}
		}
		
		if(result == null)
		{
			result = Toenga.instance.getDefaultData();
		}
		
		return result;
	}
	
	public void applyModules(LocalInstance instance, ModuleState state)
	{
		for(String parent : parents)
		{
			ToengaModel model = Toenga.instance.modelFromName(parent);
			
			if(model == null)
			{
				Toenga.debug(this.name, "Parent does not exists", parent);
			}
			else
			{
				Toenga.debug(this.name, "Copy parent...", parent);
				model.applyModules(instance, state);;
			}
		}
		
		for(ToengaModelModule module : modules)
		{
			module.apply(instance, state);
		}
	}
	
	public void copyModel(File dst)
	{
		if(!dst.exists())
		{
			dst.mkdirs();
		}
		
		for(String parent : parents)
		{
			ToengaModel model = Toenga.instance.modelFromName(parent);
			
			if(model == null)
			{
				Toenga.debug(this.name, "Parent does not exists", parent);
			}
			else
			{
				Toenga.debug(this.name, "Copy parent...", parent);
				model.copyModel(dst);
			}
		}
		
		for(ToengaModelFile file : create)
		{
			file.copy(this, dst);
		}
	}
	
	public void updateModel(File dst)
	{
		if(!dst.exists())
		{
			dst.mkdirs();
		}
		
		for(String parent : parents)
		{
			ToengaModel model = Toenga.instance.modelFromName(parent);
			
			if(model == null)
			{
				Toenga.debug(this.name, "Parent does not exists", parent);
			}
			else
			{
				Toenga.debug(this.name, "Update parent...", parent);
				model.copyModel(dst);
			}
		}
		
		if(update_remove != null)
			for(String file : update_remove)
			{
				Toenga.debug(name, "Remove file...", file);
				FileUtils.delete(new File(dst, file));
			}
		
		if(update_copy != null)
			for(ToengaModelFile file : update_copy)
			{
				file.copy(this, dst);
			}
	}
}
