package dev.vuis.cccr.mod.client.mixin;

import dev.vuis.cccr.mod.item.GunItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(
		method = "extractCrosshair",
		at = @At("HEAD"),
		cancellable = true
	)
	private void disableCrosshairWhenHoldingGun(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		if (minecraft.player != null && minecraft.player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof GunItem) {
			ci.cancel();
		}
	}
}
