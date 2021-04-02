package io.github.haykam821.voxelplace.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {
	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void preventSpawning(ItemUsageContext itemUsageContext, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(itemUsageContext.getPlayer());
		if (spawner.canInteract() && spawner.getFeatures().mobSpawning) {
			ci.setReturnValue(ActionResult.FAIL);
		}
	}

	@Inject(method = "useOnBlock", at = @At("RETURN"))
	private void handleSuccessfulSpawning(ItemUsageContext itemUsageContext, CallbackInfoReturnable<ActionResult> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(itemUsageContext.getPlayer());
		ActionResult actionResult = ci.getReturnValue();
		if (actionResult.isAccepted() && spawner.getFeatures().mobSpawning) {
			spawner.updateNextInteraction();
		}
	}

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void preventFluidSpawning(World world, PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(playerEntity);
		if (spawner.canInteract() && spawner.getFeatures().mobSpawning) {
			ItemStack handStack = playerEntity.getStackInHand(hand);
			ci.setReturnValue(TypedActionResult.fail(handStack));
		}
	}

	@Inject(method = "use", at = @At("RETURN"))
	private void handleSuccessfulFluidSpawning(World world, PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(playerEntity);
		TypedActionResult<ItemStack> actionResult = ci.getReturnValue();
		if (actionResult.getResult().isAccepted() && spawner.getFeatures().mobSpawning) {
			spawner.updateNextInteraction();
		}
	}

	@Inject(method = "spawnBaby", at = @At("HEAD"), cancellable = true)
	private void preventBabySpawning(PlayerEntity user, MobEntity mob, EntityType<? extends MobEntity> type, ServerWorld world, Vec3d pos, ItemStack stack, CallbackInfoReturnable<Optional<MobEntity>> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(user);
		if (spawner.canInteract() && spawner.getFeatures().mobSpawning) {
			ci.setReturnValue(Optional.empty());
		}
	}

	@Inject(method = "spawnBaby", at = @At("RETURN"))
	private void handleSuccessfulBabySpawning(PlayerEntity user, MobEntity mob, EntityType<? extends MobEntity> type, ServerWorld world, Vec3d pos, ItemStack stack, CallbackInfoReturnable<Optional<MobEntity>> ci) {
		NextInteractionEntity spawner = NextInteractionEntity.from(user);
		if (ci.getReturnValue().isPresent() && spawner.getFeatures().mobSpawning) {
			spawner.updateNextInteraction();
		}
	}
}