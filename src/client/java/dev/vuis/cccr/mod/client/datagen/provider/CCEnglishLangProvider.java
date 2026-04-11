package dev.vuis.cccr.mod.client.datagen.provider;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.client.registry.CCKeyMappings;
import dev.vuis.cccr.mod.registry.CCCreativeModeTabs;
import dev.vuis.cccr.mod.registry.CCItems;
import dev.vuis.cccr.mod.text.CCTexts;

public final class CCEnglishLangProvider extends FabricLanguageProvider {
    public CCEnglishLangProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup, @NonNull TranslationBuilder builder) {
        for (CCTexts.Base instance : CCTexts.instances()) {
            builder.add(instance.getKey(), instance.getEnglishText());
        }

        builder.add(CCCreativeModeTabs.KEY_GUNS, "CounterCraft Guns");

        builder.add(CCItems.DESERT_EAGLE, "Desert Eagle");

        builder.add("key.category.cccr.cccr", CounterCraft.DISPLAY_NAME);
        builder.add(CCKeyMappings.RELOAD.getName(), "Reload");
    }
}
