package dev.vuis.cccr.mod.match;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vuis.cccr.mod.registry.CCRegistries;

public abstract class Match<T extends Match<T>> {
	public static final Codec<Match<?>> CODEC = CCRegistries.MATCH_TYPE.byNameCodec().dispatch("type", Match::codec, m -> m);

	protected final String name;

	public Match(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	protected abstract MapCodec<T> codec();

	protected static <T extends Match<T>> Products.P1<RecordCodecBuilder.Mu<T>, String> getBaseProduct(RecordCodecBuilder.Instance<T> instance) {
		return instance.group(
			Codec.STRING.fieldOf("name").forGetter(Match::getName)
		);
	}
}
