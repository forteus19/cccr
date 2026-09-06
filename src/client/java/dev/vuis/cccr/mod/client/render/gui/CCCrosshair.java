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

	private static final int PART_SIZE = 16;

	public void extract(GuiGraphicsExtractor graphics, DeltaTracker delta) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null || !(player.getMainHandItem().getItem() instanceof GunItem)) {
			return;
		}

		int midX = graphics.guiWidth() / 2;
		int midY = graphics.guiHeight() / 2;
		int renderSpread = (int) (GunItemClient.currentSpread(delta.getGameTimeDeltaPartialTick(false)) * 5f);
		int centerOffset = 6;

		blitCrosshairPart(
			graphics, middle,
			midX,
			midY
		);
		blitCrosshairPart(
			graphics, top,
			midX,
			midY - renderSpread - centerOffset
		);
		blitCrosshairPart(
			graphics, bottom,
			midX,
			midY + renderSpread + centerOffset
		);
		blitCrosshairPart(
			graphics, left,
			midX - renderSpread - centerOffset,
			midY
		);
		blitCrosshairPart(
			graphics, right,
			midX + renderSpread + centerOffset,
			midY
		);
	}

	private static void blitCrosshairPart(GuiGraphicsExtractor graphics, Identifier texture, int x, int y) {
		graphics.blit(
			RenderPipelines.GUI_TEXTURED,
			texture,
			x - PART_SIZE / 2, y - PART_SIZE / 2,
			0f, 0f,
			PART_SIZE, PART_SIZE,
			PART_SIZE, PART_SIZE
		);
	}
}
