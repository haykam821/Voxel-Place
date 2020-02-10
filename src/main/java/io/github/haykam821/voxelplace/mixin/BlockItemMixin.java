package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
	@Inject(method = "place", at = @At("HEAD"), cancellable = true)
	void preventPlacing(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> ci) {
		PlayerEntity playerEntity = context.getPlayer();
		if (playerEntity != null) {
			NextInteractionEntity nextInteractionEntity = NextInteractionEntity.from(playerEntity);
			if (nextInteractionEntity.canInteract() && nextInteractionEntity.getFeatures().blockPlacing) {
				ci.setReturnValue(ActionResult.FAIL);
			}
		}
	}
}