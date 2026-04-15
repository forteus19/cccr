package dev.vuis.cccr.mod.item.component;

import dev.vuis.cccr.mod.registry.CCDataComponents;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;

public final class CCComponentTooltipProviderSetup {
	private CCComponentTooltipProviderSetup() {
		throw new AssertionError();
	}

	public static void register() {
		ItemComponentTooltipProviderRegistry.addLast(CCDataComponents.AMMO);
	}
}
