package dev.vuis.cccr.mod.item;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.data.Ammo;
import dev.vuis.cccr.mod.item.gun.FireMode;
import dev.vuis.cccr.mod.item.gun.SpreadValues;
import dev.vuis.cccr.mod.registry.CCDataComponents;
import dev.vuis.cccr.mod.text.CCTexts;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Builder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class GunItem extends CCGeoItem implements GeoItem {
	public final Settings settings;
	public final Sounds sounds;

	public final MutableObject<Object> clientData = new MutableObject<>();

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	public final MutableObject<Consumer<AnimatableManager.ControllerRegistrar>> geoRegistrarHandler = new MutableObject<>();
	public final MutableObject<Object> geoRenderProvider = new MutableObject<>();

	public GunItem(Properties properties, Settings settings, Sounds sounds) {
		super(settings.setComponentDefaults(properties));
		this.settings = settings;
		this.sounds = sounds;

		GeoItem.registerSyncedAnimatable(this);
	}

	public static Ammo getAmmo(ItemStack stack) {
		return stack.getOrDefault(CCDataComponents.AMMO, Ammo.EMPTY);
	}

	public static void setAmmo(ItemStack stack, Ammo ammo) {
		stack.set(CCDataComponents.AMMO, ammo);
	}

	public static int getReloadTicks(ItemStack stack) {
		return stack.getOrDefault(CCDataComponents.RELOAD_TICKS, 0);
	}

	public static void setReloadTicks(ItemStack stack, int reloadTicks) {
		stack.set(CCDataComponents.RELOAD_TICKS, reloadTicks);
	}

	@Override
	public void inventoryTick(@NonNull ItemStack stack, @NonNull ServerLevel level, @NonNull Entity owner, @Nullable EquipmentSlot slot) {
		super.inventoryTick(stack, level, owner, slot);

		if (level.isClientSide() || slot != EquipmentSlot.MAINHAND) {
			return;
		}

		int reloadTicks = getReloadTicks(stack);
		if (reloadTicks > 0) {
			setReloadTicks(stack, reloadTicks - 1);
		}

		GeoItem.getOrAssignId(stack, level);
	}

	@Override
	public @NonNull Component getName(@NonNull ItemStack stack) {
		Component name = super.getName(stack);

		if (getReloadTicks(stack) > 0) {
			name = name.copy().append(" ").append(CCTexts.GUN_MESSAGE_RELOADING.get());
		}

		return name;
	}

	@Override
	public void registerControllers(AnimatableManager.@NonNull ControllerRegistrar controllers) {
		Consumer<AnimatableManager.ControllerRegistrar> registrarHandler = geoRegistrarHandler.get();
		if (registrarHandler != null) {
			registrarHandler.accept(controllers);
		} else {
			CounterCraft.LOGGER.error("No registrar handler for gun {}", BuiltInRegistries.ITEM.getKey(this));
		}
	}

	@Override
	public @NonNull AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoCache;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createGeoRenderer(@NonNull Consumer consumer) {
		consumer.accept(geoRenderProvider.get());
	}

	@Override
	public boolean isPerspectiveAware() {
		return true;
	}

	@Builder
	public record Settings(
		float damage,
		List<FireMode> fireModes,
		int shootDelay,
		Ammo maxAmmo,
		int reloadTicks,
		SpreadValues spread,
		boolean shootAfterSpread,
		float recoil
	) {
		public Item.Properties setComponentDefaults(Item.Properties properties) {
			return properties.component(CCDataComponents.AMMO, maxAmmo);
		}
	}

	@Builder
	public record Sounds(
		Supplier<SoundEvent> shoot,
		Supplier<SoundEvent> reload
	) {
	}
}
