package dev.vuis.cccr.mod.client.setup;

import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.object.PlayState;
import com.geckolib.constant.DataTickets;
import com.geckolib.constant.DefaultAnimations;
import com.geckolib.renderer.GeoItemRenderer;
import com.google.common.base.Suppliers;
import dev.vuis.cccr.mod.client.item.GunItemClient;
import dev.vuis.cccr.mod.client.render.item.GunItemRenderer;
import dev.vuis.cccr.mod.item.GunItem;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import org.jspecify.annotations.Nullable;

public final class CCGeoClientSetup {
	private CCGeoClientSetup() {
		throw new AssertionError();
	}

	public static void register() {
		for (Item item : BuiltInRegistries.ITEM) {
			if (item instanceof GunItem gunItem) {
				setupGunItem(gunItem);
			}
		}
	}

	private static void setupGunItem(GunItem item) {
		item.geoRegistrarHandler.setValue(controllers -> {
			controllers.add(
				new AnimationController<>(
					GunItemClient.CONTROLLER_NAME, 0,
					test -> test.getDataOrDefault(DataTickets.ITEM_RENDER_PERSPECTIVE, ItemDisplayContext.NONE).firstPerson() ? PlayState.CONTINUE : PlayState.STOP
				)
					.triggerableAnim("shoot", DefaultAnimations.ATTACK_SHOOT)
			);
		});

		item.geoRenderProvider.setValue(new GeoRenderProvider() {
			private final Supplier<GeoItemRenderer<GunItem>> renderer = Suppliers.memoize(
				() -> new GunItemRenderer(item)
			);

			@Override
			public @Nullable GeoItemRenderer<?> getGeoItemRenderer() {
				return renderer.get();
			}
		});
	}
}
