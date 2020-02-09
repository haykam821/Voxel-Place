package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(method = "onBreak", at = @At("TAIL"))
	public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity, CallbackInfo ci) {
		NextInteractionEntity.from(playerEntity).updateNextInteraction();
	}

	@Inject(method = "onPlaced", at = @At("TAIL"))
	public void onPlaced(World world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack, CallbackInfo ci) {
		if (livingEntity instanceof PlayerEntity) {
			NextInteractionEntity.from((PlayerEntity) livingEntity).updateNextInteraction();
		}
	}
}