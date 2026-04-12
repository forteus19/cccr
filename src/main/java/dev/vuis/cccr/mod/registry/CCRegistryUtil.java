package dev.vuis.cccr.mod.registry;

import dev.vuis.cccr.mod.CounterCraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

final class CCRegistryUtil {
	private CCRegistryUtil() {
		throw new AssertionError();
	}

	static <T> ResourceKey<T> createKey(ResourceKey<? extends Registry<T>> registryKey, String path) {
		return ResourceKey.create(registryKey, CounterCraft.id(path));
	}
}
