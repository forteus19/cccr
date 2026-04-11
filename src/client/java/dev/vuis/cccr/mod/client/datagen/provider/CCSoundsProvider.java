package dev.vuis.cccr.mod.client.datagen.provider;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.NonNull;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.registry.CCSoundEvents;

public final class CCSoundsProvider extends FabricSoundsProvider {
    public CCSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public @NonNull String getName() {
        return "Sounds";
    }

    @Override
    protected void configure(HolderLookup.@NonNull Provider registryLookup, @NonNull SoundExporter exporter) {
        add(exporter, CCSoundEvents.ITEM_GUN_EMPTY, "item/gun/empty", false);
        add(exporter, CCSoundEvents.ITEM_GUN_DESERT_EAGLE_RELOAD, "item/gun/desert_eagle/reload", false);
        add(exporter, CCSoundEvents.ITEM_GUN_DESERT_EAGLE_SHOOT, "item/gun/desert_eagle/shoot", false);
    }

    private static void add(@NonNull SoundExporter exporter, @NonNull SoundEvent soundEvent, @NonNull String soundFile, boolean subtitles) {
        exporter.add(
            soundEvent,
            (subtitles ? SoundTypeBuilder.of(soundEvent) : SoundTypeBuilder.of())
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(CounterCraft.id(soundFile)))
        );
    }
}
