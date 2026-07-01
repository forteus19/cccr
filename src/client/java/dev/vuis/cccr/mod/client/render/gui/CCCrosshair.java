package dev.vuis.cccr.mod.client.render.gui;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.client.item.GunItemClient;
import dev.vuis.cccr.mod.item.GunItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public record CCCrosshair(
	Identifier middle,
	Identifier top,
	Identifier bottom,
	Identifier left,
	Identifier right
) {
	public static final CCCrosshair DEFAULT = new CCCrosshair(
		CounterCraft.id("textures/gui/crosshair/middle.png"),
		CounterCraft.id("textures/gui/crosshair/top.png"),
		CounterCraft.id("textures/gui/crosshair/bottom.png"),
		CounterCraft.id("textures/gui/crosshair/left.png"),
		CounterCraft.id("textures/gui/crosshair/right.png")
	);

	private static final int SIZE = 16;
	private static final int TEXTURE_SIZE = 32;

	public void extract(GuiGraphicsExtractor graphics, DeltaTracker delta) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null || !(player.getMainHandItem().getItem() instanceof GunItem)) {
			return;
		}

		int midX = graphics.guiWidth() / 2;
		int midY = graphics.guiHeight() / 2;
		int renderSpread = (int) (GunItemClient.currentSpread(delta.getGameTimeDeltaPartialTick(false)) * 5f);

		blitCrosshairPart(graphics, middle, midX - 16, midY - 16);
		blitCrosshairPart(graphics, top, midX - 16, midY - (20 + renderSpread));
		blitCrosshairPart(graphics, bottom, midX - 16, midY - (7 - renderSpread));
		blitCrosshairPart(graphics, left, midX - (20 + renderSpread), midY - 16);
		blitCrosshairPart(graphics, right, midX - (7 - renderSpread), midY - 16);
	}

	private static void blitCrosshairPart(GuiGraphicsExtractor graphics, Identifier texture, int x, int y) {
		graphics.blit(
			RenderPipelines.GUI_TEXTURED, texture,
			x, y, 0f, 0f,
			SIZE, SIZE,
			TEXTURE_SIZE, TEXTURE_SIZE
		);
	}
}
