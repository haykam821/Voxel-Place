package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.component.CooldownComponent;
import io.github.haykam821.voxelplace.component.VoxelPlaceComponentInitializer;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(ItemFrameEntity.class)
public class ItemFrameEntityMixin {
	@Inject(method = "interact", at = @At("HEAD"), cancellable = true)
	private void preventEditing(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(playerEntity);
		if (component.hasCooldown() && component.getFeatures().itemFrameEditing) {
			ci.setReturnValue(ActionResult.PASS);
		}
	}

	@Inject(method = "interact", at = @At("RETURN"))
	private void handleSuccessfulEditing(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(playerEntity);
		if (ci.getReturnValue().isAccepted() && component.getFeatures().itemFrameEditing) {
			component.startCooldown();
		}
	}
}