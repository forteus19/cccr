package dev.vuis.cccr.mod.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jspecify.annotations.NonNull;

public record Ammo(int loaded, int reserve) implements TooltipProvider {
	public static final Ammo EMPTY = new Ammo(0, 0);

	public static final Codec<Ammo> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loaded").forGetter(Ammo::loaded),
			ExtraCodecs.NON_NEGATIVE_INT.fieldOf("reserve").forGetter(Ammo::reserve)
		).apply(
			instance, Ammo::new
		));
	public static final StreamCodec<ByteBuf, Ammo> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, Ammo::loaded,
		ByteBufCodecs.VAR_INT, Ammo::reserve,
		Ammo::new
	);

	public Ammo withLoaded(int loaded) {
		return new Ammo(loaded, this.reserve);
	}

	@Override
	public void addToTooltip(
		Item.@NonNull TooltipContext context,
		@NonNull Consumer<Component> consumer,
		@NonNull TooltipFlag flag,
		@NonNull DataComponentGetter components
	) {
		consumer.accept(
			Component.literal("--- AMMO ---")
				.withStyle(ChatFormatting.GRAY)
		);
		consumer.accept(
			Component.literal("Loaded: ")
				.append(
					Component.literal(Integer.toString(loaded))
						.withStyle(loaded > 0 ? ChatFormatting.GREEN : ChatFormatting.RED)
				)
		);
		consumer.accept(
			Component.literal("Reserve: ")
				.append(
					Component.literal(Integer.toString(reserve))
						.withStyle(reserve > 0 ? ChatFormatting.GREEN : ChatFormatting.RED)
				)
		);
	}
}
