package dev.vuis.cccr.mod.client.setup;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.client.render.gui.CCCrosshair;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

public final class CCHudRenderingSetup {
	private CCHudRenderingSetup() {
		throw new AssertionError();
	}

	public static void register() {
		HudElementRegistry.attachElementAfter(
			VanillaHudElements.CROSSHAIR,
			CounterCraft.id("gun_crosshair"),
			CCCrosshair.DEFAULT::extract
		);
	}
}
