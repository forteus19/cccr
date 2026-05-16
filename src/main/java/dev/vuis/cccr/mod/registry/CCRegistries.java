package dev.vuis.cccr.mod.registry;

import com.mojang.serialization.MapCodec;
import dev.vuis.cccr.mod.match.Match;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;

public final class CCRegistries {
	public static final Registry<MapCodec<? extends Match<?>>> MATCH_TYPE = FabricRegistryBuilder.create(CCRegistryKeys.MATCH_TYPE).buildAndRegister();

	private CCRegistries() {
		throw new AssertionError();
	}

	public static void init() {
	}
}
