package io.github.haykam821.voxelplace.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.voxelplace.NextInteractionEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin {
	@Inject(method = "interact", at = @At("HEAD"), cancellable = true)
	void preventEditing(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<Boolean> ci) {
		NextInteractionEntity editor = NextInteractionEntity.from(playerEntity);
		if (editor.canInteract() && editor.getFeatures().itemFrameEditing) {
			ci.setReturnValue(false);
		}
	}

	@Inject(method = "interact", at = @At("RETURN"))
	void handleSuccessfulEditing(PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<Boolean> ci) {
		NextInteractionEntity editor = NextInteractionEntity.from(playerEntity);
		if (ci.getReturnValue() && editor.getFeatures().itemFrameEditing) {
			editor.updateNextInteraction();
		}
	}
}