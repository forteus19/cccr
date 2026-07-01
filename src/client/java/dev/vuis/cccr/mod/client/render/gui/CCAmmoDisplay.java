package dev.vuis.cccr.mod.client.render.gui;

import dev.vuis.cccr.mod.client.item.GunItemClient;
import dev.vuis.cccr.mod.data.Ammo;
import dev.vuis.cccr.mod.item.GunItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

public final class CCAmmoDisplay {
	private static final int WIDTH = 80;
	private static final int HEIGHT = 17;
	private static final int PADDING = 2;
	private static final int TEXT_PADDING = 4;

	private static final Component AMMO_SEPARATOR = Component.literal("/");
	private static final Style AMMO_EMPTY_STYLE = Style.EMPTY.withColor(ChatFormatting.RED);

	private CCAmmoDisplay() {
		throw new AssertionError();
	}

	public static void extract(GuiGraphicsExtractor graphics, DeltaTracker delta) {
		Minecraft minecraft = Minecraft.getInstance();

		LocalPlayer player = minecraft.player;
		if (player == null) {
			return;
		}
		ItemStack heldStack = player.getMainHandItem();
		if (!(heldStack.getItem() instanceof GunItem gunItem)) {
			return;
		}

		Ammo ammo = GunItem.getAmmo(heldStack);
		GunItemClient.Data clientData = GunItemClient.getClientData(gunItem);

		int x = graphics.guiWidth() - WIDTH - PADDING;
		int y = graphics.guiHeight() - HEIGHT - PADDING;

		graphics.fill(
			x, y,
			x + WIDTH, y + HEIGHT,
			0x7F000000
		);

		Font font = minecraft.font;
		int textY = y + HEIGHT / 2 - 3;

		if (GunItem.getReloadTicks(heldStack) <= 0) {
			Component ammoComponent = Component.empty()
				.append(ammoCountComponent(ammo.loaded()))
				.append(AMMO_SEPARATOR)
				.append(ammoCountComponent(ammo.reserve()));

			graphics.text(
				font,
				ammoComponent,
				x + TEXT_PADDING, textY,
				0xFFFFFFFF, true
			);

			String fireModeName = gunItem.fireModes[clientData.fireModeIndex].toString();

			graphics.text(
				font,
				fireModeName,
				x + WIDTH - TEXT_PADDING - font.width(fireModeName), textY,
				0xFFFFFFFF, true
			);
		} else {
			graphics.centeredText(
				font,
				"Reloading...",
				x + WIDTH / 2, textY,
				0xFFFFFFFF
			);
		}
	}

	private static Component ammoCountComponent(int count) {
		MutableComponent component = Component.literal(Integer.toString(count));
		if (count <= 0) {
			component.setStyle(AMMO_EMPTY_STYLE);
		}
		return component;
	}
}
