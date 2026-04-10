package dev.vuis.cccr.mod.net;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import dev.vuis.cccr.mod.net.packet.ServerboundGunReloadPacket;

public final class CCServerboundPacketHandlerSetup {
    private CCServerboundPacketHandlerSetup() {
        throw new AssertionError();
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ServerboundGunReloadPacket.TYPE, ServerboundGunReloadPacket::handle);
    }
}
