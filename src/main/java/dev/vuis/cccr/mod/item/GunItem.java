package dev.vuis.cccr.mod.item;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import dev.vuis.cccr.mod.server.bullet.BulletHitResult;
import dev.vuis.cccr.mod.server.bullet.ServerBulletUtil;

public class GunItem extends Item {
	public GunItem(Properties properties) {
		super(properties);
	}

	@Override
	public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.PASS;
		}

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
					3,
					0.0, 0.0, 0.0,
					1.0
				);
			}
		}

		if (hitResult.entity() != null) {
			hitResult.entity().getEntity().hurtServer(serverLevel, serverLevel.damageSources().playerAttack(player), 2);
		}

		return InteractionResult.CONSUME;
	}
}
