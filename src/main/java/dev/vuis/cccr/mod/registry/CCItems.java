package dev.vuis.cccr.mod.registry;

import dev.vuis.cccr.mod.data.Ammo;
import dev.vuis.cccr.mod.item.GunItem;
import dev.vuis.cccr.mod.item.gun.FireMode;
import dev.vuis.cccr.mod.item.gun.SpreadValues;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class CCItems {
	public static final GunItem DESERT_EAGLE = register(
		"desert_eagle", new Item.Properties().stacksTo(1),
		properties -> new GunItem(
			properties,
			new GunItem.Settings()
				.damage(6)
				.fireModes(FireMode.SEMI)
				.shootDelay(5)
				.maxAmmo(new Ammo(7, 14))
				.reloadTicks(35)
				.spread(new SpreadValues(14f, 1.2f, 2f, 6f, 0.75f, 1f, 8f, 100f))
				.soundShoot(() -> CCSoundEvents.ITEM_GUN_DESERT_EAGLE_SHOOT)
				.soundReload(() -> CCSoundEvents.ITEM_GUN_DESERT_EAGLE_RELOAD)
		)
	);
	public static final GunItem AK_74 = register(
		"ak_74", new Item.Properties().stacksTo(1),
		properties -> new GunItem(
			properties,
			new GunItem.Settings()
				.damage(8)
				.fireModes(FireMode.AUTO, FireMode.SEMI)
				.shootDelay(2)
				.maxAmmo(new Ammo(31, 62))
				.reloadTicks(35)
				.spread(new SpreadValues(1.8f, 0.5f, 0.9f, 2f, 1.2f, 2.2f, 6.5f, 100f))
				.soundShoot(() -> CCSoundEvents.ITEM_GUN_DESERT_EAGLE_SHOOT)
				.soundReload(() -> CCSoundEvents.ITEM_GUN_DESERT_EAGLE_RELOAD)
		)
	);

	private CCItems() {
		throw new AssertionError();
	}

	static <T extends Item> T register(String id, Item.Properties properties, Function<Item.Properties, T> constructor) {
		ResourceKey<Item> key = CCRegistryUtil.createKey(Registries.ITEM, id);
		T item = constructor.apply(properties.setId(key));
		Registry.register(BuiltInRegistries.ITEM, key, item);
		return item;
	}

	public static void init() {
	}
}
