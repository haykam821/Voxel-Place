package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements NextInteractionEntity {
	long nextInteraction = 0;

	PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	public void updateNextInteraction() {
		this.setNextInteraction(System.currentTimeMillis() + 5000);
	}

	void setNextInteraction(long newNext) {
		nextInteraction = newNext;
	}

	public boolean canInteract() {
		return nextInteraction > System.currentTimeMillis();
	}

	@Inject(method = "canMine", at = @At("HEAD"), cancellable = true)
	void preventMining(World world, BlockPos blockPos, GameMode gamemode, CallbackInfoReturnable<Boolean> ci) {
		if (canInteract()) {
			ci.setReturnValue(true);
		}
	}

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	void preventAttacking(Entity entity, CallbackInfo ci) {
		if (canInteract()) {
			ci.cancel();
		}
	}

	@Override
	public void onAttacking(Entity entity) {
		updateNextInteraction();
		super.onAttacking(entity);
	}
}