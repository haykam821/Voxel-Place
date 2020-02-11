package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(SpawnEggItem.class)
public abstract class SpawnEggItemMixin {
	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	void preventSpawning(ItemUsageContext itemUsageContext, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(itemUsageContext.getPlayer());
		if (spawner.canInteract() && spawner.getFeatures().mobSpawning) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "useOnBlock", at = @At("RETURN"))
	void handleSuccessfulSpawning(ItemUsageContext itemUsageContext, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(itemUsageContext.getPlayer());
		ActionResult actionResult = ci.getReturnValue();
		if (actionResult.isAccepted() && spawner.getFeatures().mobSpawning) {
			spawner.updateNextInteraction();
		}
	}

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	void preventFluidSpawning(World world, PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(playerEntity);
		if (spawner.canInteract() && spawner.getFeatures().mobSpawning) {
			ItemStack handStack = playerEntity.getStackInHand(hand);
			ci.setReturnValue(TypedActionResult.fail(handStack));
		}
	}

	@Inject(method = "use", at = @At("RETURN"))
	void handleSuccessfulFluidSpawning(World world, PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(playerEntity);
		TypedActionResult<ItemStack> actionResult = ci.getReturnValue();
		if (actionResult.getResult().isAccepted() && spawner.getFeatures().mobSpawning) {
			spawner.updateNextInteraction();
		}
	}
}