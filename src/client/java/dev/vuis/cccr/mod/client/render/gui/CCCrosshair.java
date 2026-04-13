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
import net.minecraft.world.entity.EquipmentSlot;

public record CCCrosshair(
	Identifier middle,
	Identifier top,
	Identifier bottom,
	Identifier left,
	Identifier right
) {
	private static final int TEXTURE_SIZE = 16;
	public static final CCCrosshair DEFAULT = new CCCrosshair(
		CounterCraft.id("textures/gui/crosshair/middle.png"),
		CounterCraft.id("textures/gui/crosshair/top.png"),
		CounterCraft.id("textures/gui/crosshair/bottom.png"),
		CounterCraft.id("textures/gui/crosshair/left.png"),
		CounterCraft.id("textures/gui/crosshair/right.png")
	);

	public void extract(GuiGraphicsExtractor graphics, DeltaTracker delta) {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player == null || !(player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof GunItem)) {
			return;
		}

		int midX = graphics.guiWidth() / 2;
		int midY = graphics.guiHeight() / 2;
		int renderSpread = (int) (GunItemClient.currentSpread(delta.getGameTimeDeltaPartialTick(false)) * 5f);

		graphics.blit(
			RenderPipelines.GUI_TEXTURED, middle,
			midX - 8, midY - 8, 0, 0,
			TEXTURE_SIZE, TEXTURE_SIZE,
			TEXTURE_SIZE, TEXTURE_SIZE
		);
		graphics.blit(
			RenderPipelines.GUI_TEXTURED, top,
			midX - 8, midY - (14 + renderSpread), 0, 0,
			TEXTURE_SIZE, TEXTURE_SIZE,
			TEXTURE_SIZE, TEXTURE_SIZE
		);
		graphics.blit(
			RenderPipelines.GUI_TEXTURED, bottom,
			midX - 8, midY - (2 - renderSpread),
			0, 0,
			TEXTURE_SIZE, TEXTURE_SIZE,
			TEXTURE_SIZE, TEXTURE_SIZE
		);
		graphics.blit(
			RenderPipelines.GUI_TEXTURED, left,
			midX - (14 + renderSpread), midY - 8,
			0, 0,
			TEXTURE_SIZE, TEXTURE_SIZE,
			TEXTURE_SIZE, TEXTURE_SIZE
		);
		graphics.blit(
			RenderPipelines.GUI_TEXTURED, right,
			midX - (2 - renderSpread), midY - 8,
			0, 0,
			TEXTURE_SIZE, TEXTURE_SIZE,
			TEXTURE_SIZE, TEXTURE_SIZE
		);
	}
}
