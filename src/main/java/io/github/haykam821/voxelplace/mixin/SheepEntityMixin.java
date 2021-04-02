package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(SheepEntity.class)
public class SheepEntityMixin {
	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	private void preventShearing(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity shearer = NextInteractionEntity.from(playerEntity);
		if (shearer.canInteract() && shearer.getFeatures().sheepShearing) {
			ci.setReturnValue(ActionResult.PASS);
		}
	}

	@Inject(method = "interactMob", at = @At("RETURN"))
	private void handleSuccessfulShearing(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity shearer = NextInteractionEntity.from(playerEntity);
		if (ci.getReturnValue().isAccepted() && shearer.getFeatures().sheepShearing) {
			shearer.updateNextInteraction();
		}
	}
}