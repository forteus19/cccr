package dev.vuis.cccr.mod.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record TeamPair<T>(
	T counterTerrorists,
	T terrorists
) {
	public static <T> Codec<TeamPair<T>> codec(Codec<T> tCodec) {
		return RecordCodecBuilder.create(instance -> instance
			.group(
				tCodec.fieldOf("counter_terrorists").forGetter(TeamPair::counterTerrorists),
				tCodec.fieldOf("terrorists").forGetter(TeamPair::terrorists)
			).apply(
				instance, TeamPair::new
			));
	}
}
