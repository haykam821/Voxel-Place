package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

@Mixin(FlintAndSteelItem.class)
public abstract class FlintAndSteelItemMixin {
	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	void preventLighting(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity lighter = NextInteractionEntity.from(context.getPlayer());
		if (lighter.canInteract() && lighter.getFeatures().fireLighting) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "useOnBlock", at = @At("RETURN"))
	void handleSuccessfulLighting(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity lighter = NextInteractionEntity.from(context.getPlayer());
		if (ci.getReturnValue().isAccepted() && lighter.getFeatures().fireLighting) {
			lighter.updateNextInteraction();
		}
	}
}