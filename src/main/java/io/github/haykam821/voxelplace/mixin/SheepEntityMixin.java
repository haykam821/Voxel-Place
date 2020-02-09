package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin {
	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	void preventShearing(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<Boolean> ci) {
		NextInteractionEntity shearer = NextInteractionEntity.from(playerEntity);
		if (shearer.canInteract()) {
			ci.setReturnValue(false);
		}
	}

	@Inject(method = "interactMob", at = @At("RETURN"))
	void handleSuccessfulShearing(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<Boolean> ci) {
		NextInteractionEntity shearer = NextInteractionEntity.from(playerEntity);
		if (ci.getReturnValue()) {
			shearer.updateNextInteraction();
		}
	}
}