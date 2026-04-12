package dev.vuis.cccr.mod.text;

import com.demonwav.mcdev.annotations.Translatable;
import com.google.common.base.Suppliers;
import dev.vuis.cccr.mod.CounterCraft;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;

public final class CCTexts {
	private static final List<Base> INSTANCES = new ArrayList<>();

	public static final Singleton KEY_CATEGORY_CCCR = new Singleton("key.category.cccr.cccr", CounterCraft.DISPLAY_NAME);
	public static final Singleton KEY_RELOAD = new Singleton("key.cccr.reload", "Reload");
	public static final Singleton KEY_SWITCH_FIRE_MODE = new Singleton("key.cccr.switchFireMode", "Switch Fire Mode");

	public static final Singleton GUN_MESSAGE_RELOADING = new Singleton("cccr.gun.message.reloading", "(Reloading)");

	private CCTexts() {
		throw new AssertionError();
	}

	public static List<Base> instances() {
		return Collections.unmodifiableList(INSTANCES);
	}

	public static abstract class Base {
		protected final String key;
		private final String englishText;

		protected Base(@Translatable(foldMethod = true) String key, String englishText) {
			this.key = key;
			this.englishText = englishText;

			INSTANCES.add(this);
		}

		public String getKey() {
			return key;
		}

		public String getEnglishText() {
			return englishText;
		}
	}

	public static final class Singleton extends Base {
		private final Supplier<Component> componentSupplier;

		public Singleton(@Translatable(foldMethod = true) String key, String englishText) {
			super(key, englishText);
			this.componentSupplier = Suppliers.memoize(() -> Component.translatable(key));
		}

		public Component get() {
			return componentSupplier.get();
		}
	}

	public static final class Dynamic extends Base {
		private final int numArgs;

		public Dynamic(@Translatable(foldMethod = true) String key, String englishText, int numArgs) {
			super(key, englishText);
			this.numArgs = numArgs;
		}

		public Component get(Object... args) {
			checkArgsLength(args);
			return Component.translatable(key, args);
		}

		public Component stringy(Object... args) {
			checkArgsLength(args);
			return Component.translatableEscape(key, args);
		}

		private void checkArgsLength(Object... args) {
			if (args.length != numArgs) {
				throw new IllegalArgumentException("Invalid args for dynamic text");
			}
		}
	}
}
