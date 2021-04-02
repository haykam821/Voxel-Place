package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(DyeItem.class)
public class DyeItemMixin {
	@Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
	private void preventDyeing(ItemStack itemStack, PlayerEntity playerEntity, LivingEntity livingEntity, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity dyeUser = NextInteractionEntity.from(playerEntity);
		if (dyeUser.canInteract() && dyeUser.getFeatures().sheepDyeing) {
			ci.setReturnValue(ActionResult.PASS);
		}
	}

	@Inject(method = "useOnEntity", at = @At("RETURN"))
	private void handleSuccessfulDyeing(ItemStack itemStack, PlayerEntity playerEntity, LivingEntity livingEntity, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity dyeUser = NextInteractionEntity.from(playerEntity);
		if (ci.getReturnValue().isAccepted() && dyeUser.getFeatures().sheepDyeing) {
			dyeUser.updateNextInteraction();
		}
	}
}