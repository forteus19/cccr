package dev.vuis.cccr.mod.net.packet;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.data.Ammo;
import dev.vuis.cccr.mod.item.GunItem;
import dev.vuis.cccr.mod.item.gun.BulletBlockHit;
import dev.vuis.cccr.mod.item.gun.BulletEntityHit;
import io.netty.buffer.ByteBuf;
import java.util.List;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public record ServerboundGunShootPacket(
	Vec3 from,
	Vec3 to,
	List<BulletBlockHit> blockHits,
	List<BulletEntityHit> entityHits
) implements CustomPacketPayload {
	public static final Type<ServerboundGunShootPacket> TYPE = new Type<>(CounterCraft.id("gun_shoot"));

	public static final StreamCodec<ByteBuf, ServerboundGunShootPacket> STREAM_CODEC = StreamCodec.composite(
		Vec3.STREAM_CODEC, ServerboundGunShootPacket::from,
		Vec3.STREAM_CODEC, ServerboundGunShootPacket::to,
		BulletBlockHit.STREAM_CODEC.apply(ByteBufCodecs.list()), ServerboundGunShootPacket::blockHits,
		BulletEntityHit.STREAM_CODEC.apply(ByteBufCodecs.list()), ServerboundGunShootPacket::entityHits,
		ServerboundGunShootPacket::new
	);

	@Override
	public @NonNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(ServerPlayNetworking.Context context) {
		ServerPlayer player = context.player();
		ServerLevel level = player.level();

		ItemStack heldStack = player.getMainHandItem();
		if (!(heldStack.getItem() instanceof GunItem gunItem)) {
			return;
		}

		if (GunItem.getReloadTicks(heldStack) > 0) {
			return;
		}

		Ammo ammo = GunItem.getAmmo(heldStack);
		if (ammo.loaded() == 0) {
			return;
		}
		GunItem.setAmmo(heldStack, ammo.withLoaded(ammo.loaded() - 1));

		for (BulletBlockHit blockHit : blockHits) {
			blockHit.handle(level, player);
		}

		for (BulletEntityHit entityHit : entityHits) {
			entityHit.handle(level, player);
		}
	}
}
