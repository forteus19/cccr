package dev.vuis.cccr.mod.registry;

import dev.vuis.cccr.mod.CounterCraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public final class CCSoundEvents {
	public static final SoundEvent ITEM_GUN_EMPTY = register("item.gun.empty");
	public static final SoundEvent ITEM_GUN_DESERT_EAGLE_RELOAD = register("item.gun.desert_eagle.reload");
	public static final SoundEvent ITEM_GUN_DESERT_EAGLE_SHOOT = register("item.gun.desert_eagle.shoot");

	private CCSoundEvents() {
		throw new AssertionError();
	}

	private static SoundEvent register(String id) {
		Identifier identifier = CounterCraft.id(id);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
	}

	public static void init() {
	}
}
