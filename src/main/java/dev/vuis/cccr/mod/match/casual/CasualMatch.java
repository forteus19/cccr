package dev.vuis.cccr.mod.match.casual;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vuis.cccr.mod.match.Match;
import dev.vuis.cccr.mod.match.TeamPair;
import dev.vuis.cccr.mod.util.Pose;
import java.util.List;

public final class CasualMatch extends Match<CasualMatch> {
	public static final MapCodec<CasualMatch> CODEC = RecordCodecBuilder.mapCodec(instance ->
		getBaseProduct(instance).and(
			TeamPair.codec(Pose.CODEC.listOf()).fieldOf("spawns").forGetter(CasualMatch::getSpawns)
		).apply(
			instance, CasualMatch::new
		));

	private final TeamPair<List<Pose>> spawns;

	public CasualMatch(String name, TeamPair<List<Pose>> spawns) {
		super(name);
		this.spawns = spawns;
	}

	@Override
	protected MapCodec<CasualMatch> codec() {
		return CODEC;
	}

	public TeamPair<List<Pose>> getSpawns() {
		return spawns;
	}
}
