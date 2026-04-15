package dev.vuis.cccr.mod.item;

import dev.vuis.cccr.mod.data.Ammo;
import dev.vuis.cccr.mod.item.gun.FireMode;
import dev.vuis.cccr.mod.item.gun.SpreadValues;
import dev.vuis.cccr.mod.registry.CCDataComponents;
import dev.vuis.cccr.mod.text.CCTexts;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class GunItem extends Item {
	public final float damage;
	public final @NonNull FireMode[] fireModes;
	public final int shootDelay;
	public final @NonNull Ammo maxAmmo;
	public final int maxReloadTicks;
	public final @NonNull SpreadValues spread;
	public final boolean shootAfterSpread;
	public final float recoil;

	public final @NonNull Supplier<SoundEvent> soundShoot;
	public final @NonNull Supplier<SoundEvent> soundReload;

	public final @NonNull AtomicReference<Object> clientData = new AtomicReference<>();

	public GunItem(Properties properties, Settings gunSettings) {
		super(gunSettings.setComponentDefaults(properties));

		this.damage = Objects.requireNonNull(gunSettings.damage, "damage");
		this.fireModes = Objects.requireNonNull(gunSettings.fireModes, "fireModes");
		this.shootDelay = Objects.requireNonNull(gunSettings.shootDelay, "shootDelay");
		this.maxAmmo = Objects.requireNonNull(gunSettings.maxAmmo, "maxAmmo");
		this.maxReloadTicks = Objects.requireNonNull(gunSettings.reloadTicks, "reloadTicks");
		this.spread = Objects.requireNonNull(gunSettings.spread, "spread");
		this.shootAfterSpread = Objects.requireNonNull(gunSettings.shootAfterSpread, "shootAfterSpread");
		this.recoil = Objects.requireNonNull(gunSettings.recoil, "recoil");

		this.soundShoot = Objects.requireNonNull(gunSettings.soundShoot, "soundShoot");
		this.soundReload = Objects.requireNonNull(gunSettings.soundReload, "soundReload");
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
	}

	@Override
	public @NonNull Component getName(@NonNull ItemStack stack) {
		Component name = super.getName(stack);

		if (getReloadTicks(stack) > 0) {
			name = name.copy().append(" ").append(CCTexts.GUN_MESSAGE_RELOADING.get());
		}

		return name;
	}

	public static final class Settings {
		private Float damage;
		private FireMode[] fireModes;
		private Integer shootDelay;
		private Ammo maxAmmo;
		private Integer reloadTicks;
		private SpreadValues spread;
		private Boolean shootAfterSpread;
		private Float recoil;

		private Supplier<SoundEvent> soundShoot;
		private Supplier<SoundEvent> soundReload;

		public Settings damage(float damage) {
			this.damage = damage;
			return this;
		}

		public Settings fireModes(FireMode... fireModes) {
			this.fireModes = fireModes;
			return this;
		}

		public Settings shootDelay(int shootDelay) {
			this.shootDelay = shootDelay;
			return this;
		}

		public Settings maxAmmo(Ammo maxAmmo) {
			this.maxAmmo = maxAmmo;
			return this;
		}

		public Settings reloadTicks(int reloadTicks) {
			this.reloadTicks = reloadTicks;
			return this;
		}

		public Settings spread(SpreadValues spread) {
			this.spread = spread;
			return this;
		}

		public Settings shootAfterSpread(boolean shootAfterSpread) {
			this.shootAfterSpread = shootAfterSpread;
			return this;
		}

		public Settings recoil(float recoil) {
			this.recoil = recoil;
			return this;
		}

		public Settings soundShoot(Supplier<SoundEvent> soundShoot) {
			this.soundShoot = soundShoot;
			return this;
		}

		public Settings soundReload(Supplier<SoundEvent> soundReload) {
			this.soundReload = soundReload;
			return this;
		}

		public Item.Properties setComponentDefaults(Item.Properties properties) {
			return properties.component(CCDataComponents.AMMO, maxAmmo);
		}
	}
}
