package dev.vuis.cccr.mod.item.tooltip;

import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;

import dev.vuis.cccr.mod.registry.CCDataComponents;

public final class CCTooltipProviderSetup {
    private CCTooltipProviderSetup() {
        throw new AssertionError();
    }

    public static void register() {
        ItemComponentTooltipProviderRegistry.addLast(CCDataComponents.AMMO);
    }
}
