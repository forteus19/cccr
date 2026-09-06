package dev.vuis.cccr.mod.client.datagen.provider;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.renderer.internal.GeckolibItemSpecialRenderer;
import dev.vuis.cccr.mod.registry.CCItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.world.item.Item;
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
		generateGeckolibItem(generators, CCItems.DESERT_EAGLE);
		generateGeckolibItem(generators, CCItems.AK_74);
	}

	@SuppressWarnings("UnstableApiUsage")
	private <I extends Item & GeoAnimatable> void generateGeckolibItem(@NonNull ItemModelGenerators generators, @NonNull I item) {
		generators.itemModelOutput.accept(
			item,
			ItemModelUtils.specialModel(
				ModelLocationUtils.getModelLocation(item),
				new GeckolibItemSpecialRenderer.Unbaked<>()
			)
		);
	}
}
