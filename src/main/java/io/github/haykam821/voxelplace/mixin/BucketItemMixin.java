package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(BucketItem.class)
public class BucketItemMixin {
	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void preventEmptying(World world, PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		ItemStack itemStack = playerEntity.getStackInHand(hand);
		NextInteractionEntity bucketer = NextInteractionEntity.from(playerEntity);
		if (bucketer.canInteract() && bucketer.getFeatures().bucketEmptying) {
			ci.setReturnValue(TypedActionResult.fail(itemStack));
		}
	}

	@Inject(method = "use", at = @At("RETURN"))
	private void handleSuccessfulEmptying(World world, PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		NextInteractionEntity bucketer = NextInteractionEntity.from(playerEntity);
		if (ci.getReturnValue().getResult().isAccepted() && bucketer.getFeatures().bucketEmptying) {
			bucketer.updateNextInteraction();
		}
	}
}