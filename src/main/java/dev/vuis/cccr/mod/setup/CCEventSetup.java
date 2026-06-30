package dev.vuis.cccr.mod.setup;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class CCEventSetup {
	private CCEventSetup() {
		throw new AssertionError();
	}

	public static void register() {
		CommandRegistrationCallback.EVENT.register(CCCommandSetup::register);
	}
}
