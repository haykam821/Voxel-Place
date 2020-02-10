package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin {
	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	void preventPotting(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity potter = NextInteractionEntity.from(playerEntity);
		if (potter.canInteract()) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "onUse", at = @At("RETURN"))
	void handleSuccessfulPotting(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity potter = NextInteractionEntity.from(playerEntity);
		ActionResult actionResult = ci.getReturnValue();
		if (actionResult.isAccepted()) {
			potter.updateNextInteraction();
		}
	}
}