package dev.vuis.cccr.mod.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import dev.vuis.cccr.mod.CounterCraft;

final class CCRegistryUtil {
    private CCRegistryUtil() {
        throw new AssertionError();
    }

    static <T> ResourceKey<T> createKey(ResourceKey<? extends Registry<T>> registryKey, String path) {
        return ResourceKey.create(registryKey, CounterCraft.id(path));
    }
}
