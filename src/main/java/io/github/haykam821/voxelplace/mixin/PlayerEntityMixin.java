package io.github.haykam821.voxelplace.mixin;

import java.util.Timer;
import java.util.TimerTask;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import io.github.haykam821.voxelplace.config.ModConfig;
import io.github.haykam821.voxelplace.config.ModConfig.FeaturesConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements NextInteractionEntity {
	long nextInteraction = 0;
	Timer timer = new Timer();

	ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

	PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	public void updateNextInteraction() {
		this.setNextInteraction(System.currentTimeMillis() + config.cooldown);

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					PlayerEntity playerEntity = ((PlayerEntity) (Object) PlayerEntityMixin.this);
					if (!playerEntity.world.isClient) {
						playerEntity.playSound(Instrument.HARP.getSound(), SoundCategory.NEUTRAL, 1.0F, config.pitch);
					}
				} catch (Exception error) {
					System.out.println("Failed to play cooldown sound: " + error);
				}
			}
		};
		timer.schedule(task, Math.max(0, config.cooldown));
	}

	void setNextInteraction(long newNext) {
		nextInteraction = newNext;
	}

	public boolean canInteract() {
		return nextInteraction > System.currentTimeMillis();
	}

	public FeaturesConfig getFeatures() {
		return config.features;
	}

	// Persist next interaction
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	public void readNextInteractionFromTag(CompoundTag compoundTag, CallbackInfo ci) {
		setNextInteraction(compoundTag.getLong("NextInteraction"));
	}
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	public void writeNextInteractionToTag(CompoundTag compoundTag, CallbackInfo ci) {
		compoundTag.putLong("NextInteraction", nextInteraction);
	}

	@Inject(method = "canMine", at = @At("HEAD"), cancellable = true)
	void preventMining(World world, BlockPos blockPos, GameMode gamemode, CallbackInfoReturnable<Boolean> ci) {
		if (canInteract() && getFeatures().blockBreaking) {
			ci.setReturnValue(true);
		}
	}

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	void preventAttacking(Entity entity, CallbackInfo ci) {
		if (canInteract() && getFeatures().attacking) {
			ci.cancel();
		}
	}

	@Override
	public void onAttacking(Entity entity) {
		if (getFeatures().attacking) {
			updateNextInteraction();
		}
		super.onAttacking(entity);
	}
}