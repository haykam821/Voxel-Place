package io.github.haykam821.voxelplace;

import net.minecraft.entity.player.PlayerEntity;

public interface NextInteractionEntity {
	void updateNextInteraction();
	boolean canInteract();

	static NextInteractionEntity from(PlayerEntity self) {
		return (NextInteractionEntity) self;
	}
}