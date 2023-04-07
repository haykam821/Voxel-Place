package io.github.haykam821.voxelplace.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import io.github.haykam821.voxelplace.config.ModConfig;
import io.github.haykam821.voxelplace.config.ModConfig.FeaturesConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class CooldownComponent implements PlayerComponent<Component>, ServerTickingComponent, AutoSyncedComponent {
	private static final ModConfig CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	private static final SoundEvent FINISH_SOUND = Instrument.HARP.getSound().value();

	private final PlayerEntity player;
	private int cooldownTicks = 0;

	public CooldownComponent(PlayerEntity player) {
		this.player = player;
	}

	// Queries
	public boolean hasCooldown() {
		return this.cooldownTicks > 0;
	}

	public FeaturesConfig getFeatures() {
		return CONFIG.features;
	}

	// Utilities
	public void startCooldown() {
		this.cooldownTicks = CONFIG.cooldown;
	}

	private void onCooldownEnded() {
		this.player.playSound(FINISH_SOUND, SoundCategory.NEUTRAL, 1, CONFIG.pitch);
	}

	// Ticking
	@Override
	public void serverTick() {
		if (this.cooldownTicks > 0) {
			this.cooldownTicks -= 1;
			VoxelPlaceComponentInitializer.COOLDOWN.sync(this.player);

			if (this.cooldownTicks == 0) {
				this.onCooldownEnded();
			}
		}
	}

	// Serialization
	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.cooldownTicks = nbt.getInt("CooldownTicks");
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("CooldownTicks", this.cooldownTicks);
	}

	// Syncing
	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return player.equals(this.player);
	}

	@Override
    public void applySyncPacket(PacketByteBuf buf) {
		this.cooldownTicks = buf.readInt();
	}

	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
		buf.writeInt(this.cooldownTicks);
	}
}
