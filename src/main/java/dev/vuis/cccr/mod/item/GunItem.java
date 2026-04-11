package dev.vuis.cccr.mod.item;

import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import dev.vuis.cccr.mod.data.Ammo;
import dev.vuis.cccr.mod.registry.CCDataComponents;
import dev.vuis.cccr.mod.server.bullet.BulletHitResult;
import dev.vuis.cccr.mod.server.bullet.ServerBulletUtil;
import dev.vuis.cccr.mod.text.CCTexts;

public class GunItem extends Item {
	private final int damage;
	private final @NonNull Ammo maxAmmo;
	private final int maxReloadTicks;
	private final @NonNull Supplier<SoundEvent> soundShoot;
	private final @NonNull Supplier<SoundEvent> soundReload;

	public GunItem(Properties properties, Settings gunSettings) {
		super(gunSettings.setComponentDefaults(properties));

		this.damage = Objects.requireNonNull(gunSettings.damage, "damage");
        this.maxAmmo = Objects.requireNonNull(gunSettings.maxAmmo, "maxAmmo");
		this.maxReloadTicks = Objects.requireNonNull(gunSettings.reloadTicks, "reloadTicks");
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

		int reloadTicks = getReloadTicks(stack);
		if (reloadTicks > 0) {
			setReloadTicks(stack, reloadTicks - 1);
		}
	}

	@Override
	public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.PASS;
		}

		ItemStack stack = player.getItemInHand(hand);

		if (getReloadTicks(stack) > 0) {
			return InteractionResult.CONSUME;
		}

		Ammo ammo = getAmmo(stack);
		if (ammo.loaded() == 0) {
			return InteractionResult.CONSUME;
		}
		setAmmo(stack, ammo.withLoaded(ammo.loaded() - 1));

		serverLevel.playSound(
			null, player,
			soundShoot.get(),
			SoundSource.PLAYERS,
			2f, 1f
		);

		Vec3 start = player.getEyePosition();
		Vec3 end = start.add(player.getViewVector(1f).scale(64));

		BulletHitResult hitResult = ServerBulletUtil.bullet(
			serverLevel, new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, player)
		);

		BlockHitResult blockHit = hitResult.block();

		if (blockHit.getType() == HitResult.Type.BLOCK) {
			Vec3 location = blockHit.getLocation();
			BlockState hitState = serverLevel.getBlockState(blockHit.getBlockPos());

			if (!hitState.isAir()) {
				serverLevel.playSound(
					null,
					location.x, location.y, location.z,
					hitState.getSoundType().getBreakSound(), SoundSource.BLOCKS
				);
				serverLevel.sendParticles(
					new BlockParticleOption(ParticleTypes.BLOCK, hitState),
					location.x, location.y, location.z,
					5,
					0.0, 0.0, 0.0,
					1.0
				);
			}
		}

		if (hitResult.entity() != null && hitResult.entity().getEntity() instanceof LivingEntity entity) {
			entity.hurtTime = 0;
			entity.invulnerableTime = 0;
			if (entity.hurtServer(serverLevel, serverLevel.damageSources().playerAttack(player), damage)) {
				entity.setDeltaMovement(0.0, 0.0, 0.0);
			}
		}

		return InteractionResult.CONSUME;
	}

	@Override
	public @NonNull Component getName(@NonNull ItemStack stack) {
		Component name = super.getName(stack);

		if (getReloadTicks(stack) > 0) {
			name = name.copy().append(" ").append(CCTexts.GUN_MESSAGE_RELOADING.get());
		}

		return name;
	}

	public void reload(@NonNull Level level, @NonNull Player player, @NonNull ItemStack stack) {
		int reloadTicks = getReloadTicks(stack);
		if (reloadTicks > 0) {
			return;
		}

		Ammo ammo = getAmmo(stack);
		if (ammo.loaded() >= maxAmmo.loaded() || ammo.reserve() == 0) {
			return;
		}

		setReloadTicks(stack, maxReloadTicks);

		int usedAmmo = maxAmmo.loaded() - ammo.loaded();
		setAmmo(stack, new Ammo(
			Math.min(ammo.loaded() + ammo.reserve(), maxAmmo.loaded()),
			Math.max(ammo.reserve() - usedAmmo, 0)
		));

		level.playSound(
			null, player,
			soundReload.get(),
			SoundSource.PLAYERS,
			1f, 1f
		);
	}

	public static final class Settings {
		private Integer damage;
		private Ammo maxAmmo;
		private Integer reloadTicks;
		private Supplier<SoundEvent> soundShoot;
		private Supplier<SoundEvent> soundReload;

		public Settings damage(int damage) {
			this.damage = damage;
			return this;
		}

		public Settings maxAmmo(Ammo maxAmmo) {
			this.maxAmmo = maxAmmo;
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

		public Settings reloadTicks(int reloadTicks) {
			this.reloadTicks = reloadTicks;
			return this;
		}

		public Item.Properties setComponentDefaults(Item.Properties properties) {
			return properties.component(CCDataComponents.AMMO, maxAmmo);
		}
	}
}
