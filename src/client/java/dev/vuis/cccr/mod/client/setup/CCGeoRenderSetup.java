package dev.vuis.cccr.mod.client.setup;

import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.renderer.GeoItemRenderer;
import com.google.common.base.Suppliers;
import dev.vuis.cccr.mod.client.render.item.GunItemRenderer;
import dev.vuis.cccr.mod.item.GunItem;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public final class CCGeoRenderSetup {
	private CCGeoRenderSetup() {
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
