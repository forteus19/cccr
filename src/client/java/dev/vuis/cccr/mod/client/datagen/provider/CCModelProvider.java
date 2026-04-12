package dev.vuis.cccr.mod.client.datagen.provider;

import dev.vuis.cccr.mod.registry.CCItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import org.jspecify.annotations.NonNull;

public final class CCModelProvider extends FabricModelProvider {
	public CCModelProvider(FabricPackOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(@NonNull BlockModelGenerators generators) {
	}

	@Override
	public void generateItemModels(@NonNull ItemModelGenerators generators) {
		generators.declareCustomModelItem(CCItems.DESERT_EAGLE);
		generators.declareCustomModelItem(CCItems.AK_74);
	}
}
