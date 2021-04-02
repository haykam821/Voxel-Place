package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.component.CooldownComponent;
import io.github.haykam821.voxelplace.component.VoxelPlaceComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@Inject(method = "place", at = @At("HEAD"), cancellable = true)
	private void preventPlacing(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> ci) {
		PlayerEntity playerEntity = context.getPlayer();
		if (playerEntity != null) {
			CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(playerEntity);
			if (component.hasCooldown() && component.getFeatures().blockPlacing) {
				ci.setReturnValue(ActionResult.FAIL);
			}
		}
	}
}