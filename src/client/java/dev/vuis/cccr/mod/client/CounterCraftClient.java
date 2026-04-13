package dev.vuis.cccr.mod.client;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.client.event.CCClientEvents;
import dev.vuis.cccr.mod.client.registry.CCKeyMappings;
import dev.vuis.cccr.mod.client.render.gui.CCHudRenderingSetup;
import net.fabricmc.api.ClientModInitializer;

public final class CounterCraftClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CounterCraft.LOGGER.info("Initializing for client");

		CCClientEvents.register();

		CounterCraft.LOGGER.info("Registering key mappings");
		CCKeyMappings.init();
		CounterCraft.LOGGER.info("Registering HUD renderers");
		CCHudRenderingSetup.register();
	}
}
