package dev.vuis.cccr.mod.item.gun;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public record BulletBlockHit(
	Vec3 hitPos,
	BlockPos blockPos
) {
	public static final StreamCodec<ByteBuf, BulletBlockHit> STREAM_CODEC = StreamCodec.composite(
		Vec3.STREAM_CODEC, BulletBlockHit::hitPos,
		BlockPos.STREAM_CODEC, BulletBlockHit::blockPos,
		BulletBlockHit::new
	);

	public void handle(ServerLevel level, Entity shootingEntity) {
		BlockState hitState = level.getBlockState(blockPos);

		if (hitState.isAir()) {
			return;
		}

		level.playSound(
			shootingEntity,
			hitPos.x, hitPos.y, hitPos.z,
			hitState.getSoundType().getBreakSound(), SoundSource.BLOCKS
		);
		level.sendParticles(
			new BlockParticleOption(ParticleTypes.BLOCK, hitState),
			hitPos.x, hitPos.y, hitPos.z,
			5,
			0.0, 0.0, 0.0,
			1.0
		);
	}
}
