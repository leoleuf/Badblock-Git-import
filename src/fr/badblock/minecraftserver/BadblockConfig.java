package fr.badblock.minecraftserver;

public class BadblockConfig {
	public static BadblockConfig config = null;
	
	public BadblockConfigTileEntities tileEntities = new BadblockConfigTileEntities();

	public static class BadblockConfigTileEntities {
		public boolean tickBeacon 		 = true;
		public boolean tickBrewingStand  = true;
		public boolean tickEnchantTable  = true;
		public boolean tickFurnace 		 = true;
		public boolean tickHopper 		 = true;
		public boolean tickLightDetector = true;
		public boolean tickMobSpawner 	 = true;
		public boolean tickPiston 	     = true;
	}
}
