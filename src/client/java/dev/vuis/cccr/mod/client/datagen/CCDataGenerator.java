package dev.vuis.cccr.mod.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jspecify.annotations.NonNull;

import dev.vuis.cccr.mod.client.datagen.provider.CCEnglishLangProvider;
import dev.vuis.cccr.mod.client.datagen.provider.CCModelProvider;
import dev.vuis.cccr.mod.client.datagen.provider.CCSoundsProvider;

public class CCDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(@NonNull FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(CCEnglishLangProvider::new);
		pack.addProvider(CCModelProvider::new);
		pack.addProvider(CCSoundsProvider::new);
	}
}
