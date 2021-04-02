package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.component.CooldownComponent;
import io.github.haykam821.voxelplace.component.VoxelPlaceComponentInitializer;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(AbstractSignBlock.class)
public class AbstractSignBlockMixin {
	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	private void preventSignDyeing(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> ci) {
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(playerEntity);
		if (component.hasCooldown() && component.getFeatures().signDyeing) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "onUse", at = @At("RETURN"))
	private void onUse(BlockState blockState, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(playerEntity);
		if (cir.getReturnValue().isAccepted() && component.getFeatures().signDyeing) {
			component.startCooldown();
		}
	}
}