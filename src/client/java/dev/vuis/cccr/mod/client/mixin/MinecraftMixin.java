package dev.vuis.cccr.mod.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.vuis.cccr.mod.client.event.CCClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Inject(
		method = "runTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/MouseHandler;handleAccumulatedMovement()V",
			shift = At.Shift.AFTER,
			ordinal = 0
		)
	)
	private void addStartRenderFrameHandler(
		boolean advanceGameTime,
		CallbackInfo ci,
		@Local(name = "profiler") ProfilerFiller profiler
	) {
		profiler.popPush("cccrOnStartRenderFrame");
		CCClientEvents.onStartRenderFrame((Minecraft) (Object) this);
	}
}
