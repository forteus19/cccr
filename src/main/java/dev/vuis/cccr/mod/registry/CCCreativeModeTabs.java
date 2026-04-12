package dev.vuis.cccr.mod.registry;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class CCCreativeModeTabs {
	public static final ResourceKey<CreativeModeTab> KEY_GUNS = createKey("guns");
	public static final CreativeModeTab TAB_GUNS = register(KEY_GUNS, FabricCreativeModeTab.builder()
		.icon(() -> new ItemStack(CCItems.DESERT_EAGLE))
		.title(Component.translatable("itemGroup.cccr.guns"))
		.displayItems((_, output) -> {
			output.accept(CCItems.DESERT_EAGLE);
			output.accept(CCItems.AK_74);
		})
		.build()
	);

	private CCCreativeModeTabs() {
	}

	private static ResourceKey<CreativeModeTab> createKey(String id) {
		return CCRegistryUtil.createKey(Registries.CREATIVE_MODE_TAB, id);
	}

	private static CreativeModeTab register(ResourceKey<CreativeModeTab> key, CreativeModeTab tab) {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
		return tab;
	}

	public static void init() {
	}
}
