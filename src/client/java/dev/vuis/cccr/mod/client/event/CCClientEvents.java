package dev.vuis.cccr.mod.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import dev.vuis.cccr.mod.client.item.GunItemClient;
import dev.vuis.cccr.mod.item.GunItem;

public final class CCClientEvents {
    private CCClientEvents() {
        throw new AssertionError();
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(CCClientEvents::onEndClientTick);
    }

    private static void onEndClientTick(Minecraft minecraft) {
        ClientLevel level = minecraft.level;
        LocalPlayer player = minecraft.player;
        if (level == null || player == null) {
            return;
        }

        ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (heldStack.getItem() instanceof GunItem gunItem) {
            GunItemClient.tick(minecraft, gunItem, heldStack, level, player);
        }
    }
}
