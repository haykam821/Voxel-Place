package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

@Mixin(AxeItem.class)
public class AxeItemMixin {
	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void preventStripping(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		PlayerEntity user = context.getPlayer();
		NextInteractionEntity nextInteractionEntity = NextInteractionEntity.from(user);
		if (nextInteractionEntity.canInteract() && nextInteractionEntity.getFeatures().woodStripping) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "useOnBlock", at = @At("RETURN"))
	private void handleSuccessfulStrip(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		PlayerEntity user = context.getPlayer();
		NextInteractionEntity nextInteractionEntity = NextInteractionEntity.from(user);
		if (ci.getReturnValue().isAccepted() && nextInteractionEntity.getFeatures().woodStripping) {
			nextInteractionEntity.updateNextInteraction();
		}
	}
}