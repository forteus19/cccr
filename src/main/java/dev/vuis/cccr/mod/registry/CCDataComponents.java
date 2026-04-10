package dev.vuis.cccr.mod.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.data.Ammo;

public final class CCDataComponents {
    public static final DataComponentType<Ammo> AMMO = register(
        "ammo", DataComponentType.<Ammo>builder()
            .persistent(Ammo.CODEC)
            .networkSynchronized(Ammo.STREAM_CODEC)
    );

    private CCDataComponents() {
        throw new AssertionError();
    }

    private static <T> DataComponentType<T> register(String id, DataComponentType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, CounterCraft.id(id), builder.build());
    }

    public static void init() {
    }
}
