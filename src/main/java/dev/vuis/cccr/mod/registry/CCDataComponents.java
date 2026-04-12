package dev.vuis.cccr.mod.registry;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.data.Ammo;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

public final class CCDataComponents {
	public static final DataComponentType<Ammo> AMMO = register(
		"ammo", DataComponentType.<Ammo>builder()
			.persistent(Ammo.CODEC)
			.networkSynchronized(Ammo.STREAM_CODEC)
	);
	public static final DataComponentType<Integer> RELOAD_TICKS = register(
		"reload_ticks", DataComponentType.<Integer>builder()
			.persistent(ExtraCodecs.NON_NEGATIVE_INT)
			.networkSynchronized(ByteBufCodecs.VAR_INT)
			.ignoreSwapAnimation()
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
