package dev.vuis.cccr.mod.registry;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import dev.vuis.cccr.mod.item.GunItem;

public final class CCItems {
	public static final GunItem DESERT_EAGLE = register("desert_eagle", GunItem::new, new Item.Properties().stacksTo(1));

	private CCItems() {
		throw new AssertionError();
	}

	static <T extends Item> T register(String id, Function<Item.Properties, T> constructor, Item.Properties properties) {
		ResourceKey<Item> key = CCRegistryUtil.createKey(Registries.ITEM, id);
		T item = constructor.apply(properties.setId(key));
		Registry.register(BuiltInRegistries.ITEM, key, item);
		return item;
	}

	public static void init() {
	}
}
