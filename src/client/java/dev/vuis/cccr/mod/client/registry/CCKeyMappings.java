package dev.vuis.cccr.mod.client.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import dev.vuis.cccr.mod.CounterCraft;

public final class CCKeyMappings {
    public static final KeyMapping.Category CATEGORY_CCCR = KeyMapping.Category.register(CounterCraft.id("cccr"));

    public static final KeyMapping RELOAD = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        "key.cccr.reload",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_R,
        CATEGORY_CCCR
    ));

    private CCKeyMappings() {
        throw new AssertionError();
    }

    public static void init() {
    }
}
