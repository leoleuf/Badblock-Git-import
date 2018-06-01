package fr.badblock.toenga.instance.locale;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import fr.badblock.api.common.utils.FileUtils;
import fr.badblock.api.common.utils.JsonUtils;
import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.instance.ToengaInstance;
import fr.badblock.toenga.models.ToengaModel;
import fr.badblock.toenga.modules.ModuleState;
import fr.badblock.toenga.utils.ArgumentTokenizer;
import fr.badblock.toenga.utils.ToengaUtils;
import lombok.Getter;

public class LocalInstance extends ToengaInstance
{
	public static final String STORED_DATA = ".toenga.data";
	public static final String LOCKER = ".toenga.locker";

	/**
	 * Create a new instance.
	 * @param model The model of the instance
	 * @return The instance
	 */
	public static LocalInstance createNewInstance(ToengaModel model, String cluster)
	{
		LocalInstance instance = new LocalInstance(model, cluster);
		instance.create();
		
		return instance;
	}
		
	/**
	 * Recovers the instance from its files
	 * @param folder Instance's folder
	 * @return The instance
	 */
	public static LocalInstance recoverInstance(File folder)
	{
		LocalInstance instance = new LocalInstance(folder);
		
		if(instance.isDestroyed())
			return null;
		
		if(!instance.isStarted())
			instance.applyModules(ModuleState.RECOVER);
		
		if(instance.isDestroyed())
			return null;
	
		return instance;
	}

	private String model;
	
	@Getter
	private String id, nickname, cluster;

	@Getter
	private boolean destroyed, restart;
	
	@Getter
	private Map<String, Object> data;
	
	private File folder;
	
	private LocalInstance()
	{
		this.destroyed = false;
		this.restart = false;
	}
	
	public LocalInstance(ToengaModel model, String cluster)
	{
		this();

		this.model = model.name;
		this.cluster = cluster;
		this.id = ToengaUtils.createId(model);
		this.folder = new File(Toenga.instancesFolder, this.id);
	}
	
	private LocalInstance(File file)
	{
		this();

		this.folder = file;

		LocalStoredData data = JsonUtils.load(new File(file, STORED_DATA), LocalStoredData.class);

		if(data == null || data.model == null || data.id == null || data.cluster == null)
		{
			destroy();
		}
		else
		{
			ToengaModel model = Toenga.instance.modelFromName(data.model);
			
			if(model == null)
			{
				Toenga.logErr("Unknow model, can't restore instance", data.model, data.id);
				this.destroyed = true;
				return;
			}
			
			if(data.cluster == null || !Toenga.instance.isInCluster(data.cluster))
			{
				Toenga.logErr("Bad cluster, can't restore instance", data.cluster, data.id);
				this.destroyed = true;
				return;
			}
			
			this.model = data.model;
			this.id =  data.id;
			this.nickname = data.nickname;
			this.cluster = data.cluster;
			
			if(new File(folder, LOCKER).exists())
			{
				Toenga.log("Instance '" + data.id + "' has been restored. Status: ON.");
				listenProcessEnd();
			}
			else
			{
				Toenga.log("Instance '" + data.id + "' has been restored. Status: OFF.");
			}
		}
	}
	
	private void saveData()
	{
		LocalStoredData data = new LocalStoredData(model, id, nickname, cluster);
		JsonUtils.save(new File(folder, STORED_DATA), data, true);
	}
	
	public void applyModules(ModuleState state)
	{
		getModel().applyModules(this, state);
	}

	private void create()
	{
		applyModules(ModuleState.BEFORE_COPY);
		getModel().copyModel(this.folder);
		saveData();
	}
	
	@Override
	public boolean isStarted()
	{
		return new File(this.folder, LOCKER).exists();
	}

	@Override
	public void start() throws IOException
	{
		if(isStarted())
		{
			Toenga.debug(this, "Try to start an already started instance");
			return;
		}
		
		Toenga.debug(this, "Start instance");

		data = getModel().getData();
		getModel().updateModel(this.folder);
		applyModules(ModuleState.BEFORE_START);

		String command = getModel().getCommand();
		
		if(command == null)
		{
			Toenga.logErr("Can't start instance: model has no command", getId(), model);
			return;
		}
		
		command = "java -jar {toengaprocess} " + command;
		command = ToengaUtils.formatToengaStr(command, this);
		
		String[] args = ArgumentTokenizer.tokenize(command).toArray(new String[0]);

		ProcessBuilder builder = new ProcessBuilder(args);
		builder.directory(this.folder);
		builder.start();
		
		listenProcessEnd();
		
		applyModules(ModuleState.AFTER_START);
	}

	private void listenProcessEnd()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				File locker = new File(folder, LOCKER);
				
				do
				{
					try
					{
						Thread.sleep(200L);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				while(locker.exists());
				
				processStopped();
			}
		}.start();
	}
	
	private void processStopped()
	{
		applyModules(ModuleState.AFTER_STOP);
		
		if(this.destroyed && this.folder.exists())
			destroy();
	}
	
	@Override
	public void close()
	{
		applyModules(ModuleState.BEFORE_STOP);
		new File(this.folder, LOCKER).delete();
	}

	@Override
	public void restart()
	{
		this.restart = true;
		close();
	}

	@Override
	public void destroy()
	{
		this.destroyed = true;
		this.restart = false;
		
		if(isStarted())
		{
			this.close();
		}
		else
		{
			FileUtils.delete(this.folder);
		}
	}

	@Override
	public ToengaModel getModel()
	{
		return Toenga.instance.modelFromName(this.model);
	}
	
	@Override
	public Socket getConsole()
	{
		return null;
	}
}
