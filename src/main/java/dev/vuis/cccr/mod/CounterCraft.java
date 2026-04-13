package dev.vuis.cccr.mod;

import dev.vuis.cccr.mod.component.CCComponentTooltipProviderSetup;
import dev.vuis.cccr.mod.net.CCPacketRegistrySetup;
import dev.vuis.cccr.mod.net.CCServerboundPacketHandlerSetup;
import dev.vuis.cccr.mod.registry.CCCreativeModeTabs;
import dev.vuis.cccr.mod.registry.CCDataComponents;
import dev.vuis.cccr.mod.registry.CCItems;
import dev.vuis.cccr.mod.registry.CCSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CounterCraft implements ModInitializer {
	public static final String MOD_ID = "cccr";
	public static final String DISPLAY_NAME = "CounterCraft: Condition Revived";

	public static final Logger LOGGER = LoggerFactory.getLogger(DISPLAY_NAME);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing for common");

		LOGGER.info("Registering creative mode tabs");
		CCCreativeModeTabs.init();
		LOGGER.info("Registering data components");
		CCDataComponents.init();
		LOGGER.info("Registering items");
		CCItems.init();
		LOGGER.info("Registering sound events");
		CCSoundEvents.init();

		LOGGER.info("Registering custom packets");
		CCPacketRegistrySetup.register();
		LOGGER.info("Registering serverbound packet handlers");
		CCServerboundPacketHandlerSetup.register();

		LOGGER.info("Registering tooltip providers");
		CCComponentTooltipProviderSetup.register();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(CounterCraft.MOD_ID, path);
	}
}
