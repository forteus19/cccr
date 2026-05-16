package dev.vuis.cccr.mod.command.arg;

import dev.vuis.cccr.mod.CounterCraft;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;

public final class CCCommandArgumentSetup {
	private CCCommandArgumentSetup() {
		throw new AssertionError();
	}

	public static void register() {
		ArgumentTypeRegistry.registerArgumentType(
			CounterCraft.id("match"),
			MatchArgument.class,
			SingletonArgumentInfo.contextAware(MatchArgument::match)
		);
	}
}
