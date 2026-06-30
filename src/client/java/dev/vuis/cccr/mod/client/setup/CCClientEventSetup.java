package dev.vuis.cccr.mod.client.setup;

import dev.vuis.cccr.mod.client.event.CCClientTickHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class CCClientEventSetup {
	private CCClientEventSetup() {
		throw new AssertionError();
	}

	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(CCClientTickHandler::onEndClientTick);
	}
}
