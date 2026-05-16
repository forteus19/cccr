package dev.vuis.cccr.mod.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class CCCommandSetup {
	private CCCommandSetup() {
		throw new AssertionError();
	}

	public static void register() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, context, _) -> {
			CCCommand.register(dispatcher, context);
		}));
	}
}
