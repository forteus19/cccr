package dev.vuis.cccr.mod.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import dev.vuis.cccr.mod.client.registry.CCKeyMappings;
import dev.vuis.cccr.mod.item.GunItem;
import dev.vuis.cccr.mod.net.packet.ServerboundGunReloadPacket;

public final class CCClientEvents {
    private CCClientEvents() {
        throw new AssertionError();
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(CCClientEvents::onEndClientTick);
    }

    private static void onEndClientTick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }

        while (CCKeyMappings.RELOAD.consumeClick()) {
            ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);

            if (!(heldStack.getItem() instanceof GunItem)) {
                break;
            }

            ClientPlayNetworking.send(ServerboundGunReloadPacket.INSTANCE);
        }
    }
}
