package dev.vuis.cccr.mod.client;

import net.fabricmc.api.ClientModInitializer;

import dev.vuis.cccr.mod.CounterCraft;

public final class CounterCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CounterCraft.LOGGER.info("Initializing for client");
    }
}
