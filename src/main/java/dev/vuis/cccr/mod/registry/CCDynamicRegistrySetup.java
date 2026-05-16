package dev.vuis.cccr.mod.registry;

import dev.vuis.cccr.mod.match.Match;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public final class CCDynamicRegistrySetup {
	private CCDynamicRegistrySetup() {
		throw new AssertionError();
	}

	public static void bootstrap() {
		DynamicRegistries.register(CCRegistryKeys.MATCH, Match.CODEC);
	}
}
