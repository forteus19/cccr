package dev.vuis.cccr.mod.net;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import dev.vuis.cccr.mod.net.packet.ServerboundGunReloadPacket;

public final class CCPacketRegistrySetup {
    private CCPacketRegistrySetup() {
        throw new AssertionError();
    }

    public static void register() {
        PayloadTypeRegistry.serverboundPlay().register(ServerboundGunReloadPacket.TYPE, ServerboundGunReloadPacket.STREAM_CODEC);
    }
}
