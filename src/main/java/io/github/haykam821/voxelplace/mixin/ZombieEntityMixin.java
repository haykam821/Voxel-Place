package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends Entity {
	public ZombieEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	private boolean holdingEntitySpawnEgg(PlayerEntity playerEntity, Hand hand) {
		ItemStack handStack = playerEntity.getStackInHand(hand);
		Item handItem = handStack.getItem();

		return handItem instanceof SpawnEggItem && ((SpawnEggItem) handItem).isOfSameEntityType(handStack.getTag(), this.getType());
	}

	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	void preventZombieBabySpawning(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<Boolean> ci) {
		if (holdingEntitySpawnEgg(playerEntity, hand)) {
			NextInteractionEntity spawner = NextInteractionEntity.from(playerEntity);
			if (spawner.canInteract() && spawner.getFeatures().mobSpawning) {
				ci.setReturnValue(false);
			}
		}
	}

	@Inject(method = "interactMob", at = @At("RETURN"))
	void handleSuccessfulZombieBabySpawning(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<Boolean> ci) {
		if (holdingEntitySpawnEgg(playerEntity, hand)) {
			NextInteractionEntity spawner = NextInteractionEntity.from(playerEntity);
			if (ci.getReturnValue() && spawner.getFeatures().mobSpawning) {
				spawner.updateNextInteraction();
			}
		}
	}
}