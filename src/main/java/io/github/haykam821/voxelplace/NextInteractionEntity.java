package io.github.haykam821.voxelplace;

import io.github.haykam821.voxelplace.config.ModConfig.FeaturesConfig;
import net.minecraft.entity.player.PlayerEntity;

public interface NextInteractionEntity {
	void updateNextInteraction();
	boolean canInteract();
	FeaturesConfig getFeatures();

	static NextInteractionEntity from(PlayerEntity self) {
		return (NextInteractionEntity) self;
	}
}