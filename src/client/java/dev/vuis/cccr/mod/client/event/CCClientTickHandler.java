package dev.vuis.cccr.mod.client.event;

import dev.vuis.cccr.mod.client.item.GunItemClient;
import dev.vuis.cccr.mod.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public final class CCClientTickHandler {
	private CCClientTickHandler() {
		throw new AssertionError();
	}

	public static void onEndClientTick(@NonNull Minecraft minecraft) {
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
}
