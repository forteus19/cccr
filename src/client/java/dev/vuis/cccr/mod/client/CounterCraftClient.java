package dev.vuis.cccr.mod.client;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.client.registry.CCKeyMappings;
import dev.vuis.cccr.mod.client.setup.CCGeoRenderSetup;
import dev.vuis.cccr.mod.client.setup.CCHudRenderingSetup;
import dev.vuis.cccr.mod.client.setup.CCClientEventSetup;
import net.fabricmc.api.ClientModInitializer;

public final class CounterCraftClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CounterCraft.LOGGER.info("Initializing for client");

		CounterCraft.LOGGER.info("Registering client event handlers");
		CCClientEventSetup.register();

		CounterCraft.LOGGER.info("Registering key mappings");
		CCKeyMappings.init();

		CounterCraft.LOGGER.info("Registering HUD renderers");
		CCHudRenderingSetup.register();

		CounterCraft.LOGGER.info("Registering geo renderers");
		CCGeoRenderSetup.register();
	}
}
