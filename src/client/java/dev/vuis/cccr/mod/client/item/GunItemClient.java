package dev.vuis.cccr.mod.client.item;

import dev.vuis.cccr.mod.client.registry.CCKeyMappings;
import dev.vuis.cccr.mod.data.Ammo;
import dev.vuis.cccr.mod.item.GunItem;
import dev.vuis.cccr.mod.item.gun.BulletCollision;
import dev.vuis.cccr.mod.item.gun.FireMode;
import dev.vuis.cccr.mod.net.packet.ServerboundGunReloadPacket;
import dev.vuis.cccr.mod.net.packet.ServerboundGunShootPacket;
import dev.vuis.cccr.mod.registry.CCSoundEvents;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
	private static float currentSpread = MIN_SPREAD;

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

	private static void reload() {
		ClientPlayNetworking.send(ServerboundGunReloadPacket.INSTANCE);
	}

	private static void switchFireMode(
		@NonNull ClientLevel level,
		@NonNull LocalPlayer player,
		@NonNull GunItem item,
		@NonNull Data gunData
	) {
		gunData.fireModeIndex = item.fireModes[(gunData.fireModeIndex + 1) % item.fireModes.length].ordinal();

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
		Vec3 collisionFrom = player.getEyePosition();
		Vec3 collisionTo = collisionFrom.add(player.getViewVector(1f).scale(64.0));

        var hits = BulletCollision.bullet(
            level,
            new ClipContext(
                collisionFrom, collisionTo,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY,
                CollisionContext.of(player)
            ),
            item.damage
        );

		ClientPlayNetworking.send(new ServerboundGunShootPacket(
            collisionFrom, collisionTo,
            // block hits
            hits.left().stream().map(hit -> Pair.of(hit.getLocation(), hit.getBlockPos())).toList(),
            // entity hits
            hits.right().stream().map(hit -> IntIntPair.of(hit.left().getEntity().getId(), Mth.ceil(hit.rightDouble()))).toList()
        ));

		level.playSound(
			player,
			player.getX(), player.getY(), player.getZ(),
			item.soundShoot.get(), SoundSource.PLAYERS,
			1f, 1f
		);
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

	private static void addSpread(float amount) {
		if ((currentSpread += amount) > MAX_SPREAD) {
			currentSpread = MAX_SPREAD;
		}
	}

	private static void decreaseSpread(float amount) {
		if ((currentSpread -= amount) < MIN_SPREAD) {
			currentSpread = MIN_SPREAD;
		}
	}

	private static final class Data {
		private int fireModeIndex = 0;
		private int shootCooldown = 0;
	}
}
