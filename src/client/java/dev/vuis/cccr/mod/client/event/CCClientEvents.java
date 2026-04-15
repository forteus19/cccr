package dev.vuis.cccr.mod.client.event;

import dev.vuis.cccr.mod.client.item.GunItemClient;
import dev.vuis.cccr.mod.item.GunItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public final class CCClientEvents {
	private CCClientEvents() {
		throw new AssertionError();
	}

	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(CCClientEvents::onEndClientTick);
	}

	private static void onEndClientTick(@NonNull Minecraft minecraft) {
		ClientLevel level = minecraft.level;
		LocalPlayer player = minecraft.player;
		if (level == null || player == null) {
			return;
		}

		ItemStack heldStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
		if (heldStack.getItem() instanceof GunItem gunItem) {
			GunItemClient.tick(minecraft, gunItem, heldStack, level, player);
		}
	}

	public static void onStartRenderFrame(@NonNull Minecraft minecraft) {
		ClientLevel level = minecraft.level;
		LocalPlayer player = minecraft.player;
		if (level == null || player == null) {
			return;
		}

		ItemStack heldStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
		if (heldStack.getItem() instanceof GunItem) {
			GunItemClient.renderTick(minecraft, player);
		}
	}
}
