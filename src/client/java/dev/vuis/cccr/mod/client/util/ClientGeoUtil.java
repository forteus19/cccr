package dev.vuis.cccr.mod.client.util;

import com.geckolib.GeckoLibConstants;
import com.geckolib.animatable.SingletonGeoAnimatable;
import net.minecraft.world.item.ItemStack;

public final class ClientGeoUtil {
	private ClientGeoUtil() {
		throw new AssertionError();
	}

    public static void triggerAnim(SingletonGeoAnimatable animatable, ItemStack stack, String controllerName, String animationName) {
		Long id = stack.get(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get());
		if (id == null) {
			return;
		}
		animatable.getAnimatableInstanceCache().getManagerForId(id).tryTriggerAnimation(controllerName, animationName);
	}
}
