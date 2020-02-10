package io.github.haykam821.voxelplace.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "voxelplace")
@Config.Gui.Background("minecraft:textures/block/brain_coral_block.png")
public class ModConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	public int cooldown = 5 * 1000 * 60;

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
	}
}
