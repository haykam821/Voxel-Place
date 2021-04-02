package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.component.CooldownComponent;
import io.github.haykam821.voxelplace.component.VoxelPlaceComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

@Mixin(AxeItem.class)
public class AxeItemMixin {
	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void preventStripping(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		PlayerEntity user = context.getPlayer();
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(user);
		if (component.hasCooldown() && component.getFeatures().woodStripping) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "useOnBlock", at = @At("RETURN"))
	private void handleSuccessfulStrip(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		PlayerEntity user = context.getPlayer();
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(user);
		if (ci.getReturnValue().isAccepted() && component.getFeatures().woodStripping) {
			component.startCooldown();
		}
	}
}