package fr.badblock.minecraftserver;

public class BadblockConfig {
	public static BadblockConfig config = null;
	
	public BadblockConfigTileEntities tileEntities = new BadblockConfigTileEntities();
	public BadblockConfigEntities     entities	   = new BadblockConfigEntities();
	public BadblockConfigServerTick   severTick    = new BadblockConfigServerTick();
	public BadblockConfigShutdown     shutdown	   = new BadblockConfigShutdown();
	
	public static class BadblockConfigTileEntities {
		public boolean tickBeacon 		 = true;
		public boolean tickBrewingStand  = true;
		public boolean tickFurnace 		 = true;
		public boolean tickHopper 		 = true;
		public boolean tickLightDetector = true;
		public boolean tickMobSpawner 	 = true;
		public boolean tickPiston 	     = true;
	}
	
	public static class BadblockConfigEntities {
		public boolean tickArmorStand      = true;
		public boolean tickEnderCrystal    = true;
		public boolean tickEnderSignal     = true;
		public boolean tickExperienceOrb   = true;
		public boolean tickFishingHook     = true;
		public boolean tickHanging         = true;


	}
	
	public static class BadblockConfigServerTick {
		public boolean tickWeather          = true;
		public boolean checkEntityInactives = true;
	}
	
	public static class BadblockConfigShutdown {
		public boolean savePlayers = true;
		public boolean saveWorld   = true;
	}
}
