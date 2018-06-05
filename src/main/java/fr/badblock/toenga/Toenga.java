package fr.badblock.toenga;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.badblock.api.common.tech.rabbitmq.RabbitConnector;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.utils.FileUtils;
import fr.badblock.api.common.utils.JsonUtils;
import fr.badblock.toenga.config.ToengaConfiguration;
import fr.badblock.toenga.config.ToengaConfiguration.GitConfiguration.Repository;
import fr.badblock.toenga.config.ToengaStaticConfiguration;
import fr.badblock.toenga.instance.InstanceStorage;
import fr.badblock.toenga.instance.ToengaInstance;
import fr.badblock.toenga.instance.locale.LocalInstance;
import fr.badblock.toenga.models.ToengaModel;
import fr.badblock.toenga.modules.ToengaModule;
import fr.badblock.toenga.sync.toenga.ToengaSyncManager;
import fr.badblock.toenga.utils.GitUtils;
import lombok.Getter;
import lombok.Setter;

public class Toenga
{
	public static final File dataFolder = new File("package");
	public static final File instancesFolder = new File("instances");
	public static final File statusFile = new File("status.json");
	public static final File staticConf = new File("staticConfiguration.json");

	public static final Toenga instance;
	
	static
	{
		instance = new Toenga();
		instance.restoreInstances();
	}
	
	public static void debug(Object... log)
	{
		for(Object o : log)
		{
			System.out.print(o);
			System.out.print(", ");
		}
		
		System.out.println();
	}
	
	public static void log(String log)
	{
		System.out.println(log);
	}
	
	public static void logErr(Object... log)
	{
		for(Object o : log)
		{
			System.out.print(o);
			System.out.print(",");
		}
		
		System.out.println();
	}

	private Map<String, ToengaModule> modulesByName;
	private Map<String, ToengaModel> modelsByName;
	private Map<String, ToengaInstance> instancesByName;
	
	@Getter
	private InstanceStorage storage;
	
	@Getter @Setter
	private ToengaStaticConfiguration staticConfiguration;
	
	@Getter @Setter
	private ToengaConfiguration configuration;
	
	@Getter @Setter
	private RabbitService		rabbitService;
	
	@Getter
	private String hostname;
	private ToengaStatus status;
		
	private Toenga()
	{
		this.modulesByName = ToengaModule.modules();
		this.modelsByName = new HashMap<>();
		this.instancesByName = new HashMap<>();
		
		this.status = JsonUtils.load(statusFile, ToengaStatus.class);
		this.staticConfiguration = JsonUtils.load(staticConf, ToengaStaticConfiguration.class);
		
		this.storage = new InstanceStorage();
		
		if(this.staticConfiguration == null)
		{
			Toenga.logErr("Invalid static configuration!");
			System.exit(-1);
		}
		
		if(this.staticConfiguration.getHostnameOverride() == null)
		{
			hostname = System.getenv("HOSTNAME");

			if(hostname == null)
			{
				Toenga.logErr("Static configuration has no 'hostnameOverride' field, and environment has no HOSTNAME var!");
				System.exit(-1);
			}
			else
			{
				hostname = hostname.split("\\.")[0];
			}
		}
		else
		{
			this.hostname = this.staticConfiguration.getHostnameOverride();
		}
		
		this.reloadConfiguration();

		RabbitConnector rabbitConnector = RabbitConnector.getInstance();
		RabbitService rabbitService = new RabbitService("default", configuration.getRabbitSettings());
		setRabbitService(rabbitConnector.registerService(rabbitService));
		
		new ToengaSyncManager();
	}

	public File sourceFile(String src)
	{
		return new File(dataFolder, src);
	}
	
	public ToengaModel modelFromName(String name)
	{
		return modelsByName.containsKey(name) ? modelsByName.get(name) : null;
	}
	
	public ToengaModule moduleFromName(String name)
	{
		return modulesByName.containsKey(name) ? modulesByName.get(name) : null;
	}

	public int nextInstanceId()
	{
		return this.status.nextInstance();
	}
	
	public void reloadConfiguration()
	{
		File config = new File("config.json");
		setConfiguration(JsonUtils.load(config, ToengaConfiguration.class));
		
		this.pullOrClone();
	}
	
	public File getToengaProcessJar()
	{
		String name = configuration.getToengaData().getLocations().getToengaProcessJar();
		return sourceFile(name);
	}
	
	public Map<String, Object> getDefaultData()
	{
		Map<String, Object> map = new HashMap<>();
		
		map.put("java", staticConfiguration.getJava());
		map.put("toengaprocess", getToengaProcessJar().getAbsolutePath());
		
		return map;
	}
	
	/**
	 * Start a new local instance
	 * @param model The model of the new instance
	 * @param cluster The cluster where the instance will be started
	 * @throws IOException Error when starting
	 */
	public void startInstance(ToengaModel model, String cluster) throws IOException
	{
		if(!model.startable)
		{
			Toenga.logErr("Unstartable model", model.name);
			return;
		}
		
		if(!isInCluster(cluster))
		{
			Toenga.logErr("Try to start model, but Toenga is not in the right cluster", cluster);
			return;
		}
		
		LocalInstance instance = LocalInstance.createNewInstance(model, cluster);
	
		if(instance == null || instance.isDestroyed())
		{
			Toenga.logErr("Unable to create instance", model.name);
		}
		else
		{
			registerInstance(instance);
			instance.start();
		}
	}
	
	public boolean isInCluster(String cluster)
	{
		return this.staticConfiguration.getTypes().contains(cluster);
	}
	
	private void pullOrClone()
	{
		for (Repository repository : getConfiguration().getToengaData().getRepositories())
		{
			GitUtils.pullOrClone(getConfiguration(), repository);
		}

		File config = new File("config.json");
		File nConfig = new File(dataFolder, getConfiguration().getToengaData().getLocations().getToengaConfig());

		try
		{
			if (nConfig.exists() && !FileUtils.contentEquals(nConfig, config))
			{
				FileUtils.copyFile(nConfig, config);
				reloadConfiguration();
				return;
			}
		}
		catch (Exception error)
		{
			error.printStackTrace();
		}

		
		Map<String, ToengaModel> modelsByName = new HashMap<>();
		//this.modelsByName.clear();
		
		for (String modelFolder : getConfiguration().getToengaData().getLocations().getModels())
		{
			File models = new File(dataFolder, modelFolder);

			if (!models.exists())
			{
				Toenga.debug("Unknow model folder", modelFolder);
				continue;
			}

			if (!models.isDirectory())
			{
				Toenga.debug("Model folder isn't a directory", modelFolder);
				continue;
			}

			for (File model : models.listFiles())
			{
				if (model.isDirectory())
				{
					continue;
				}
				if (model.getName().endsWith("json"))
				{
					ToengaModel conf = JsonUtils.load(model, ToengaModel.class);

					if(conf.name == null)
					{
						Toenga.debug("Model has no name", model);
					}
					else
					{
						modelsByName.put(conf.name, conf);
					}
				}
			}
		}
		
		this.modelsByName =  modelsByName;

		//FIXME reload models for existing instances

		if(!config.exists())
			JsonUtils.save(config, getConfiguration(), true);
	}
	
	private void restoreInstances()
	{
		if(!instancesFolder.exists())
			instancesFolder.mkdirs();
		
		for (File file : instancesFolder.listFiles())
		{
			ToengaInstance instance = LocalInstance.recoverInstance(file);
			
			if (instance != null)
			{
				registerInstance(instance);
			}
		}
	}
	
	private void registerInstance(ToengaInstance instance)
	{
		getStorage().getCluster(instance.getCluster()).instanceUpdate(instance);
	}
}
