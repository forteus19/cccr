package dev.vuis.cccr.mod.item.tooltip;

import dev.vuis.cccr.mod.registry.CCDataComponents;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;

public final class CCTooltipProviderSetup {
	private CCTooltipProviderSetup() {
		throw new AssertionError();
	}

	public static void register() {
		ItemComponentTooltipProviderRegistry.addLast(CCDataComponents.AMMO);
	}
}
