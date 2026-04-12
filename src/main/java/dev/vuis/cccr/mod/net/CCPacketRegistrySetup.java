package dev.vuis.cccr.mod.net;

import dev.vuis.cccr.mod.net.packet.ServerboundGunReloadPacket;
import dev.vuis.cccr.mod.net.packet.ServerboundGunShootPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class CCPacketRegistrySetup {
	private CCPacketRegistrySetup() {
		throw new AssertionError();
	}

	public static void register() {
		PayloadTypeRegistry.serverboundPlay().register(ServerboundGunReloadPacket.TYPE, ServerboundGunReloadPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(ServerboundGunShootPacket.TYPE, ServerboundGunShootPacket.STREAM_CODEC);
	}
}
