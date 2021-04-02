package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.component.CooldownComponent;
import io.github.haykam821.voxelplace.component.VoxelPlaceComponentInitializer;
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
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(playerEntity);
		if (component.hasCooldown() && component.getFeatures().sheepDyeing) {
			ci.setReturnValue(ActionResult.PASS);
		}
	}

	@Inject(method = "useOnEntity", at = @At("RETURN"))
	private void handleSuccessfulDyeing(ItemStack itemStack, PlayerEntity playerEntity, LivingEntity livingEntity, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(playerEntity);
		if (ci.getReturnValue().isAccepted() && component.getFeatures().sheepDyeing) {
			component.startCooldown();
		}
	}
}