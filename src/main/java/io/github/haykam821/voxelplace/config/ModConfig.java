package io.github.haykam821.voxelplace.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "voxelplace")
@Config.Gui.Background("minecraft:textures/block/brain_coral_block.png")
public class ModConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	public int cooldown = 20 * 5 * 60;

	@ConfigEntry.Gui.Tooltip
	public float pitch = 4.0F;

	@ConfigEntry.Category("features")
	@ConfigEntry.Gui.TransitiveObject
	public FeaturesConfig features = new FeaturesConfig();

	public static class FeaturesConfig {
		public boolean signDyeing = true;
		public boolean woodStripping = true;
		public boolean blockPlacing = true;
		public boolean blockBreaking = true;
		public boolean sheepDyeing = true;
		public boolean flowerPotting = true;
		public boolean attacking = true;
		public boolean sheepShearing = true;
		public boolean pathFlattening = true;
		public boolean mobSpawning = true;
		public boolean bucketEmptying = true;
		public boolean boatPlacing = true;
		public boolean minecartPlacing = true;
		public boolean itemFrameEditing = true;
		public boolean fireLighting = true;
	}
}
