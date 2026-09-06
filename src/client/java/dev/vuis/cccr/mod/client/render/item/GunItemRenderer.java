package dev.vuis.cccr.mod.client.render.item;

import com.geckolib.renderer.GeoItemRenderer;
import dev.vuis.cccr.mod.item.GunItem;

public class GunItemRenderer extends GeoItemRenderer<GunItem> {
	public <I extends GunItem> GunItemRenderer(I item) {
		super(item);
	}
}
