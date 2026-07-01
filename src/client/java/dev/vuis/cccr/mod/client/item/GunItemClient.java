package dev.vuis.cccr.mod.client.item;

import dev.vuis.cccr.mod.client.registry.CCKeyMappings;
import dev.vuis.cccr.mod.data.Ammo;
import dev.vuis.cccr.mod.item.GunItem;
import dev.vuis.cccr.mod.item.gun.BulletCollision;
import dev.vuis.cccr.mod.item.gun.FireMode;
import dev.vuis.cccr.mod.item.gun.SpreadValues;
import dev.vuis.cccr.mod.net.packet.ServerboundGunReloadPacket;
import dev.vuis.cccr.mod.net.packet.ServerboundGunShootPacket;
import dev.vuis.cccr.mod.registry.CCSoundEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jspecify.annotations.NonNull;

public final class GunItemClient {
	private static boolean shootDown = false;
	private static boolean reloadDown = false;
	private static boolean switchFireModeDown = false;

	private static final float MAX_SPREAD = 12f;
	private static final float MIN_SPREAD = 0.1f;
	private static float currentSpread = MAX_SPREAD;
	private static float prevSpread = currentSpread;

	private static final float RECOIL_TICK_SCALE = 0.15f;
	private static float currentRecoilPitch = 0f;
	private static float currentRecoilYawLeft = 0f;
	private static float currentRecoilYawRight = 0f;

	private GunItemClient() {
		throw new AssertionError();
	}

	public static void tick(
		@NonNull Minecraft minecraft,
		@NonNull GunItem item,
		@NonNull ItemStack stack,
		@NonNull ClientLevel level,
		@NonNull LocalPlayer player
	) {
		Data gunData = (Data) item.clientData.get();
		if (gunData == null) {
			gunData = new Data();
			item.clientData.set(gunData);
		}

		boolean shootDownPrev = shootDown;
		shootDown = minecraft.mouseHandler.isLeftPressed();

		boolean reloadDownPrev = reloadDown;
		reloadDown = CCKeyMappings.RELOAD.isDown();

		boolean switchFireModeDownPrev = switchFireModeDown;
		switchFireModeDown = CCKeyMappings.SWITCH_FIRE_MODE.isDown();

		boolean isReloading = GunItem.getReloadTicks(stack) > 0;

		if (reloadDown && !reloadDownPrev && !isReloading) {
			reload();
		}

		if (switchFireModeDown && !switchFireModeDownPrev) {
			switchFireMode(level, player, item, gunData);
		}

		if (gunData.shootCooldown > 0) {
			gunData.shootCooldown--;
		}

		prevSpread = currentSpread;
		updateSpread(player, item);
		updateRecoilValues();

		if (minecraft.screen != null) {
			return;
		}

		Ammo ammo = GunItem.getAmmo(stack);

		if (ammo.loaded() > 0) {
			if (gunData.shootCooldown == 0 && !isReloading) {
				FireMode fireMode = item.fireModes[gunData.fireModeIndex];

				if (shootDown && (fireMode == FireMode.AUTO || !shootDownPrev)) {
					shoot(level, player, item);
					gunData.shootCooldown = item.shootDelay;
				}
			}
		} else if (shootDown && !shootDownPrev && !isReloading) {
			if (ammo.reserve() > 0) {
				reload();
			} else {
				emptySound(level, player);
			}
		}
	}

	public static void renderTick(
		@NonNull Minecraft minecraft,
		@NonNull LocalPlayer player
	) {
		updateRecoilCamera(player, minecraft.getDeltaTracker());
	}

	private static void updateSpread(
		@NonNull LocalPlayer player,
		@NonNull GunItem item
	) {
		SpreadValues spread = item.spread;

		Vec3 velocity = player.getDeltaMovement();

		if (!player.onGround() || player.fallDistance >= 0.6 || velocity.y >= 0.0) {
			addSpread(spread.increaseJumping());
			return;
		}

		if (player.isCrouching()) {
			decreaseSpread(spread.decreaseShift());
			return;
		}

		if (player.isSprinting()) {
			addSpread(spread.increaseRunning(), spread.maxRunning());
			return;
		}

		double movement = (Math.abs(velocity.x * velocity.x) + Math.abs(velocity.z * velocity.z));
		if (movement > 0.004) {
			addSpread(spread.increaseWalking(), spread.maxWalking());
			return;
		}

		decreaseSpread(spread.decrease());
	}

	private static void updateRecoilValues() {
		currentRecoilPitch *= RECOIL_TICK_SCALE;
		currentRecoilYawLeft *= RECOIL_TICK_SCALE;
		currentRecoilYawRight *= RECOIL_TICK_SCALE;
	}

	private static void updateRecoilCamera(
		@NonNull LocalPlayer player,
		@NonNull DeltaTracker delta
	) {
		float tickDelta = delta.getGameTimeDeltaTicks();

		float pitchOffset = -currentRecoilPitch * tickDelta;
		player.setXRot(player.getXRot() + pitchOffset);

		float yawOffset = (-currentRecoilYawLeft + currentRecoilYawRight) * tickDelta;
		player.setYRot(player.getYRot() + yawOffset);
	}

	private static void reload() {
		ClientPlayNetworking.send(ServerboundGunReloadPacket.INSTANCE);
	}

	private static void switchFireMode(
		@NonNull ClientLevel level,
		@NonNull LocalPlayer player,
		@NonNull GunItem item,
		@NonNull Data gunData
	) {
		gunData.fireModeIndex = (gunData.fireModeIndex + 1) % item.fireModes.length;

		level.playSound(
			player,
			player.getX(), player.getY(), player.getZ(),
			SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS,
			0.75f, 4f
		);
	}

	private static void shoot(
		@NonNull ClientLevel level,
		@NonNull LocalPlayer player,
		@NonNull GunItem item
	) {
		float spreadIncreaseFired = item.spread.increaseFired();

		if (item.shootAfterSpread) {
			addSpread(spreadIncreaseFired);
		}

		Vec3 collisionFrom = player.getEyePosition();
		Vec3 collisionTo = collisionFrom.add(
			applySpread(level.getRandom(), player.getXRot(), player.getYRot()).scale(64.0)
		);

		if (!item.shootAfterSpread) {
			addSpread(spreadIncreaseFired);
		}

        BulletCollision.Result collision = BulletCollision.bullet(
            level,
            new ClipContext(
                collisionFrom, collisionTo,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY,
                CollisionContext.of(player)
            ),
            item.damage
        );

		ClientPlayNetworking.send(
			new ServerboundGunShootPacket(collisionFrom, collisionTo, collision)
		);

		applyRecoil(level.getRandom(), item.recoil);

		level.playSound(
			player,
			player.getX(), player.getY(), player.getZ(),
			item.soundShoot.get(), SoundSource.PLAYERS,
			1f, 1f
		);
	}

	private static Vec3 applySpread(
		@NonNull RandomSource random,
		float pitch,
		float yaw
	) {
		if (random.nextInt(5) != 0) {
			float yawOffset = random.nextFloat() * currentSpread * 1.5f;
			yaw += random.nextBoolean() ? yawOffset : -yawOffset;
		}

		float pitchOffset = random.nextFloat() * currentSpread * 1.5f;
		pitch -= pitchOffset;

		return Vec3.directionFromRotation(pitch, yaw);
	}

	private static void applyRecoil(
		@NonNull RandomSource random,
		float recoil
	) {
		recoil += 0.5f - random.nextFloat();

		switch (random.nextInt(3)) {
			case 0 -> currentRecoilPitch += recoil;
			case 1 -> currentRecoilYawLeft += recoil / 5f;
			case 2 -> currentRecoilYawRight += recoil / 5f;
		}

		currentRecoilPitch += recoil / 2f;
	}

	private static void emptySound(
		@NonNull ClientLevel level,
		@NonNull LocalPlayer player
	) {
		level.playSound(
			player,
			player.getX(), player.getY(), player.getZ(),
			CCSoundEvents.ITEM_GUN_EMPTY, SoundSource.PLAYERS,
			1f, 1f
		);
	}

	public static float currentSpread(float partialTick) {
		return Mth.lerp(partialTick, prevSpread, currentSpread);
	}

	public static void addSpread(float amount, float cap) {
		currentSpread = Math.min(currentSpread + amount, Math.min(cap, MAX_SPREAD));
	}

	public static void addSpread(float amount) {
		addSpread(amount, MAX_SPREAD);
	}

	public static void decreaseSpread(float amount) {
		currentSpread = Math.max(currentSpread - amount, MIN_SPREAD);
	}

	private static final class Data {
		private int fireModeIndex = 0;
		private int shootCooldown = 0;
	}
}
