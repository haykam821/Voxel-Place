package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MinecartItem;
import net.minecraft.util.ActionResult;

@Mixin(MinecartItem.class)
public abstract class MinecartItemMixin {
	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	void preventMinecartPlacing(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity minecartPlacer = NextInteractionEntity.from(context.getPlayer());
		if (minecartPlacer.canInteract() && minecartPlacer.getFeatures().minecartPlacing) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "useOnBlock", at = @At("RETURN"))
	void handleSuccessfulMinecartPlacing(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity minecartPlacer = NextInteractionEntity.from(context.getPlayer());
		if (ci.getReturnValue().isAccepted() && minecartPlacer.getFeatures().minecartPlacing) {
			minecartPlacer.updateNextInteraction();
		}
	}
}