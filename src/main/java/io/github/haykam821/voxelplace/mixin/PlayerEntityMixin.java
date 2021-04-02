package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.component.CooldownComponent;
import io.github.haykam821.voxelplace.component.VoxelPlaceComponentInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	private PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "isBlockBreakingRestricted", at = @At("HEAD"), cancellable = true)
	private void preventBlockBreaking(World world, BlockPos blockPos, GameMode gamemode, CallbackInfoReturnable<Boolean> ci) {
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(this);
		if (component.hasCooldown() && component.getFeatures().blockBreaking) {
			ci.setReturnValue(true);
		}
	}

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void preventAttacking(Entity entity, CallbackInfo ci) {
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(this);
		if (component.hasCooldown() && component.getFeatures().attacking) {
			ci.cancel();
		}
	}

	@Override
	public void onAttacking(Entity entity) {
		CooldownComponent component = VoxelPlaceComponentInitializer.COOLDOWN.get(this);
		if (component.getFeatures().attacking) {
			component.startCooldown();
		}
		super.onAttacking(entity);
	}
}