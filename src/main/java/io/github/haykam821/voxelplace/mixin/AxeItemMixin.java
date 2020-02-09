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
public abstract class AxeItemMixin {
	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	void preventStripping(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		PlayerEntity user = context.getPlayer();
		NextInteractionEntity nextInteractionEntity = NextInteractionEntity.from(user);
		if (nextInteractionEntity.canInteract()) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "useOnBlock", at = @At("RETURN"))
	public void handleSuccessfulStrip(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		PlayerEntity user = context.getPlayer();
		if (ci.getReturnValue().isAccepted()) {
			NextInteractionEntity.from(user).updateNextInteraction();
		}
	}
}