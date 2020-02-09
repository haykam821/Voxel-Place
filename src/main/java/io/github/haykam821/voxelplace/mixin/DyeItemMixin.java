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
import net.minecraft.util.Hand;

@Mixin(DyeItem.class)
public abstract class DyeItemMixin {
	@Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
	void preventDyeing(ItemStack itemStack, PlayerEntity playerEntity, LivingEntity livingEntity, Hand hand, CallbackInfoReturnable<Boolean> ci) {
		NextInteractionEntity dyeUser = NextInteractionEntity.from(playerEntity);
		if (dyeUser.canInteract()) {
			ci.setReturnValue(false);
		}
	}

	@Inject(method = "useOnEntity", at = @At("RETURN"))
	void handleSuccessfulDyeing(ItemStack itemStack, PlayerEntity playerEntity, LivingEntity livingEntity, Hand hand, CallbackInfoReturnable<Boolean> ci) {
		NextInteractionEntity dyeUser = NextInteractionEntity.from(playerEntity);
		if (ci.getReturnValue()) {
			dyeUser.updateNextInteraction();
		}
	}
}