package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

@Mixin(PassiveEntity.class)
public abstract class PassiveEntityMixin {
	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	void preventBabySpawning(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<Boolean> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(playerEntity);
		if (spawner.canInteract() && spawner.getFeatures().mobSpawning) {
			ci.setReturnValue(false);
		}
	}

	@Inject(method = "interactMob", at = @At("RETURN"))
	void handleSuccessfulBabySpawning(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<Boolean> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(playerEntity);
		if (ci.getReturnValue() && spawner.getFeatures().mobSpawning) {
			spawner.updateNextInteraction();
		}
	}
}