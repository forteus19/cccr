package dev.vuis.cccr.mod.match;

import com.mojang.serialization.MapCodec;
import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.match.casual.CasualMatch;
import dev.vuis.cccr.mod.registry.CCRegistries;
import net.minecraft.core.Registry;

public final class MatchTypes {
	private MatchTypes() {
		throw new AssertionError();
	}

	public static void bootstrap() {
		register("casual", CasualMatch.CODEC);
	}

	private static void register(String id, MapCodec<? extends Match<?>> codec) {
		Registry.register(CCRegistries.MATCH_TYPE, CounterCraft.id(id), codec);
	}
}
