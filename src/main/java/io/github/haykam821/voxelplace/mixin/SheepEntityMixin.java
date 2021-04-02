package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.component.CooldownComponent;
import io.github.haykam821.voxelplace.component.VoxelPlaceComponentInitializer;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(SheepEntity.class)
public class SheepEntityMixin {
	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	private void preventShearing(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(playerEntity);
		if (component.hasCooldown() && component.getFeatures().sheepShearing) {
			ci.setReturnValue(ActionResult.PASS);
		}
	}

	@Inject(method = "interactMob", at = @At("RETURN"))
	private void handleSuccessfulShearing(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(playerEntity);
		if (ci.getReturnValue().isAccepted() && component.getFeatures().sheepShearing) {
			component.startCooldown();
		}
	}
}