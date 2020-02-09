package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin {
	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	void preventFlattening(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		PlayerEntity user = context.getPlayer();
		NextInteractionEntity nextInteractionEntity = NextInteractionEntity.from(user);
		if (nextInteractionEntity.canInteract()) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "useOnBlock", at = @At("RETURN"))
	public void handleSuccessfulFlatten(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
		PlayerEntity user = context.getPlayer();
		if (ci.getReturnValue().isAccepted()) {
			NextInteractionEntity.from(user).updateNextInteraction();
		}
	}
}