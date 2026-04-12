package dev.vuis.cccr.mod.client.registry;

import com.mojang.blaze3d.platform.InputConstants;
import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.text.CCTexts;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class CCKeyMappings {
	public static final KeyMapping.Category CATEGORY_CCCR = KeyMapping.Category.register(CounterCraft.id("cccr"));

	public static final KeyMapping RELOAD = KeyMappingHelper.registerKeyMapping(new KeyMapping(
		CCTexts.KEY_RELOAD.getKey(),
		InputConstants.Type.KEYSYM,
		GLFW.GLFW_KEY_R,
		CATEGORY_CCCR
	));
	public static final KeyMapping SWITCH_FIRE_MODE = KeyMappingHelper.registerKeyMapping(new KeyMapping(
		CCTexts.KEY_SWITCH_FIRE_MODE.getKey(),
		InputConstants.Type.KEYSYM,
		GLFW.GLFW_KEY_G,
		CATEGORY_CCCR
	));

	private CCKeyMappings() {
		throw new AssertionError();
	}

	public static void init() {
	}
}
