package dev.vuis.cccr.mod.item.gun;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public final class WallbangValues {
	private static final Object2DoubleMap<SoundType> SOUND_TYPES = new Object2DoubleOpenHashMap<>();
	private static final Object2DoubleMap<Block> BLOCKS = new Object2DoubleOpenHashMap<>();

	static {
		SOUND_TYPES.defaultReturnValue(Double.NaN);
		BLOCKS.defaultReturnValue(Double.NaN);

		SOUND_TYPES.put(SoundType.GLASS, 0f);
	}

	private WallbangValues() {
		throw new AssertionError();
	}

	public static double get(BlockState state) {
		double reduction = SOUND_TYPES.getDouble(state.getSoundType());
		if (!Double.isNaN(reduction)) {
			return reduction;
		}

		reduction = BLOCKS.getDouble(state.getBlock());
		if (!Double.isNaN(reduction)) {
			return reduction;
		} else {
			return 1f;
		}
	}
}
