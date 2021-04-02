package io.github.haykam821.voxelplace.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import io.github.haykam821.voxelplace.Main;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class VoxelPlaceComponentInitializer implements EntityComponentInitializer {
	private static final Identifier COOLDOWN_ID = new Identifier(Main.MOD_ID, "cooldown");
	public static final ComponentKey<CooldownComponent> COOLDOWN = ComponentRegistry.getOrCreate(COOLDOWN_ID, CooldownComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(COOLDOWN, CooldownComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
	}
}
