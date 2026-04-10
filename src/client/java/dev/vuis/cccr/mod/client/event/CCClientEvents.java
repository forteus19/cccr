package dev.vuis.cccr.mod.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;

import dev.vuis.cccr.mod.client.registry.CCKeyMappings;
import dev.vuis.cccr.mod.registry.CCSoundEvents;

public final class CCClientEvents {
    private CCClientEvents() {
        throw new AssertionError();
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (CCKeyMappings.RELOAD.consumeClick()) {
                if (client.player == null) {
                    return;
                }

                client.player.sendSystemMessage(Component.literal("Reloading..."));
                client.getSoundManager().play(new EntityBoundSoundInstance(
                    CCSoundEvents.ITEM_GUN_DESERT_EAGLE_RELOAD,
                    SoundSource.PLAYERS,
                    1f, 1f,
                    client.player,
                    0L
                ));
            }
        });
    }
}
