package dev.vuis.cccr.mod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.vuis.cccr.mod.registry.CCCreativeTabs;
import dev.vuis.cccr.mod.registry.CCItems;

public final class CounterCraft implements ModInitializer {
	public static final String MOD_ID = "cccr";
	public static final String DISPLAY_NAME = "CounterCraft: Condition Revived";

	public static final Logger LOGGER = LoggerFactory.getLogger(DISPLAY_NAME);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing for common");

		LOGGER.info("Registering creative mode tabs");
		CCCreativeTabs.init();
		LOGGER.info("Registering items");
		CCItems.init();
	}
}
