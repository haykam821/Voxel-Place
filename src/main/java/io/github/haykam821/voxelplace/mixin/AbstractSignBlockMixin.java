package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(AbstractSignBlock.class)
public abstract class AbstractSignBlockMixin {
	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	void preventSignDyeing(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity nextInteractionEntity = NextInteractionEntity.from(playerEntity);
		if (nextInteractionEntity.canInteract() && nextInteractionEntity.getFeatures().signDyeing) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "onUse", at = @At("RETURN"))
	public void onUse(BlockState blockState, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		NextInteractionEntity nextInteractionEntity = NextInteractionEntity.from(playerEntity);
		if (cir.getReturnValue().isAccepted() && nextInteractionEntity.getFeatures().signDyeing) {
			nextInteractionEntity.updateNextInteraction();
		}
	}
}