package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(BoatItem.class)
public class BoatItemMixin {
	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void preventBoatPlacing(World world, PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		ItemStack itemStack = playerEntity.getStackInHand(hand);
		NextInteractionEntity boatPlacer = NextInteractionEntity.from(playerEntity);
		if (boatPlacer.canInteract() && boatPlacer.getFeatures().boatPlacing) {
			ci.setReturnValue(TypedActionResult.fail(itemStack));
		}
	}

	@Inject(method = "use", at = @At("RETURN"))
	private void handleSuccessfulBoatPlacing(World world, PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		NextInteractionEntity boatPlacer = NextInteractionEntity.from(playerEntity);
		if (ci.getReturnValue().getResult().isAccepted() && boatPlacer.getFeatures().boatPlacing) {
			boatPlacer.updateNextInteraction();
		}
	}
}