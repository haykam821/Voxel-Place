package io.github.haykam821.voxelplace.mixin;

import java.util.Timer;
import java.util.TimerTask;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements NextInteractionEntity {
	long nextInteraction = 0;
	Timer timer = new Timer();

	PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	public void updateNextInteraction() {
		this.setNextInteraction(System.currentTimeMillis() + 5000);

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					((PlayerEntity) (Object) PlayerEntityMixin.this).playSound(Instrument.PLING.getSound(), SoundCategory.MASTER, 3.0F, 1.0F);
				} catch (Exception error) {
					System.out.println("Failed to play cooldown sound: " + error);
				}
			}
		};
		timer.schedule(task, 5000);
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