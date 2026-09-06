package dev.vuis.cccr.mod.item.gun;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public final class WallbangValues {
	private static final Object2FloatMap<SoundType> SOUND_TYPES = new Object2FloatOpenHashMap<>();
	private static final Object2FloatMap<Block> BLOCKS = new Object2FloatOpenHashMap<>();

	static {
		SOUND_TYPES.defaultReturnValue(Float.NaN);
		BLOCKS.defaultReturnValue(Float.NaN);

		SOUND_TYPES.put(SoundType.GLASS, 0f);
	}

	private WallbangValues() {
		throw new AssertionError();
	}

	public static float get(BlockState state) {
		float reduction = BLOCKS.getFloat(state.getSoundType());
		if (!Float.isNaN(reduction)) {
			return reduction;
		}

		reduction = SOUND_TYPES.getFloat(state.getBlock());
		if (!Float.isNaN(reduction)) {
			return reduction;
		} else {
			return 1f;
		}
	}
}
