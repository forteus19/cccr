package dev.vuis.cccr.mod.net;

import dev.vuis.cccr.mod.net.packet.ServerboundGunReloadPacket;
import dev.vuis.cccr.mod.net.packet.ServerboundGunShootPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class CCServerboundPacketHandlerSetup {
	private CCServerboundPacketHandlerSetup() {
		throw new AssertionError();
	}

	public static void register() {
		ServerPlayNetworking.registerGlobalReceiver(ServerboundGunReloadPacket.TYPE, ServerboundGunReloadPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(ServerboundGunShootPacket.TYPE, ServerboundGunShootPacket::handle);
	}
}
