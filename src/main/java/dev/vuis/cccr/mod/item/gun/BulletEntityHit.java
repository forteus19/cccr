package dev.vuis.cccr.mod.item.gun;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;

public record BulletEntityHit(
	int entityId,
	float damage
) {
	public static final StreamCodec<ByteBuf, BulletEntityHit> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, BulletEntityHit::entityId,
		ByteBufCodecs.FLOAT, BulletEntityHit::damage,
		BulletEntityHit::new
	);

	public static BulletEntityHit from(EntityHitResult hitResult, float damage) {
		return new BulletEntityHit(
			hitResult.getEntity().getId(),
			damage
		);
	}

	public void handle(ServerLevel level, Player shootingPlayer) {
		Entity entity = level.getEntity(entityId);
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}

		livingEntity.hurtTime = 0;
		livingEntity.invulnerableTime = 0;
		if (livingEntity.hurtServer(level, level.damageSources().playerAttack(shootingPlayer), damage)) {
			livingEntity.setDeltaMovement(0.0, 0.0, 0.0);
		}
	}
}
