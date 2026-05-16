package dev.vuis.cccr.mod.registry;

import com.mojang.serialization.MapCodec;
import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.match.Match;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class CCRegistryKeys {
	public static final ResourceKey<Registry<MapCodec<? extends Match<?>>>> MATCH_TYPE = createRegistryKey("match_type");
	public static final ResourceKey<Registry<Match<?>>> MATCH = createRegistryKey("match");

	private CCRegistryKeys() {
		throw new AssertionError();
	}

	private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
		return ResourceKey.createRegistryKey(CounterCraft.id(name));
	}
}
