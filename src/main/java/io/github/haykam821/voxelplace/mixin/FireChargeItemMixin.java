package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

@Mixin(FireChargeItem.class)
public class FireChargeItemMixin {
	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void preventChargedLighting(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity lighter = NextInteractionEntity.from(context.getPlayer());
		if (lighter.canInteract() && lighter.getFeatures().fireLighting) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "useOnBlock", at = @At("RETURN"))
	private void handleSuccessfulChargedLighting(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity lighter = NextInteractionEntity.from(context.getPlayer());
		if (ci.getReturnValue().isAccepted() && lighter.getFeatures().fireLighting) {
			lighter.updateNextInteraction();
		}
	}
}