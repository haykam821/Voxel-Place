package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.TallBlockItem;
import net.minecraft.util.ActionResult;

@Mixin(TallBlockItem.class)
public class TallBlockItemMixin {
	@Inject(method = "place", at = @At("HEAD"), cancellable = true)
	private void preventTallPlacing(ItemPlacementContext context, BlockState blockState, CallbackInfoReturnable<ActionResult> ci) {
		PlayerEntity playerEntity = context.getPlayer();
		if (playerEntity != null) {
			NextInteractionEntity nextInteractionEntity = NextInteractionEntity.from(playerEntity);
			if (nextInteractionEntity.canInteract() && nextInteractionEntity.getFeatures().blockPlacing) {
				ci.setReturnValue(ActionResult.FAIL);
			}
		}
	}
	
	@Inject(method = "place", at = @At("RETURN"))
	private void handleSuccessfulTallPlace(ItemPlacementContext context, BlockState blockState, CallbackInfoReturnable<Boolean> ci) {
		if (context != null) {
			PlayerEntity placer = context.getPlayer();
			if (placer != null) {
				NextInteractionEntity nextInteractionPlacer = NextInteractionEntity.from(placer);
				if (ci.getReturnValue() && nextInteractionPlacer.getFeatures().blockPlacing) {
					nextInteractionPlacer.updateNextInteraction();
				}
			} 
		}
	}
}