package fr.badblock.toenga.models;

import java.io.File;
import java.io.IOException;

import fr.badblock.api.common.utils.FileUtils;
import fr.badblock.toenga.Toenga;

public class ToengaModelFile
{
	public String src;
	public String dst;
	
	public void copy(ToengaModel model, File dst)
	{
		File src = Toenga.instance.sourceFile(this.src);
		dst = new File(dst, this.dst);
		
		if(!src.exists())
		{
			Toenga.debug(model.name, "Try to copy unknow file", src);
			return;
		}
		
		try
		{
			if(src.isDirectory())
			{
				FileUtils.copyDirectory(src, dst);
			}
			else
			{
				FileUtils.copyFile(src, dst);
			}
			
			Toenga.debug(model.name, "Copy file success", src, dst);
		}
		catch (IOException e)
		{
			Toenga.debug(model.name, "Error while copying file", src, dst, e);
		}
	}
}
