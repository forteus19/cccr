package dev.vuis.cccr.mod.item.gun;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class BulletCollision {
	private BulletCollision() {
		throw new AssertionError();
	}

	public static Result bullet(Level level, ClipContext clipContext, float initialDamage) {
		Vec3 from = clipContext.getFrom();
		Vec3 to = clipContext.getTo();

		List<BlockHitResult> blockHits = new ArrayList<>();
		Map<BlockPos, BlockState> blockStateCache = new HashMap<>();

		BlockHitResult lastBlockHit = BlockGetter.traverseBlocks(
			from, to, clipContext,
			(context, pos) -> {
				BlockPos copiedPos = new BlockPos(pos);

				BlockState blockState = level.getBlockState(copiedPos);
				if (blockState.isAir()) {
					return null;
				}

				BlockHitResult blockHit = context.getBlockShape(blockState, level, copiedPos)
					.clip(context.getFrom(), context.getTo(), copiedPos);
				if (blockHit != null) {
					blockHits.add(blockHit);
					blockStateCache.put(copiedPos, blockState);
					return null;
				}

				FluidState fluidState = level.getFluidState(copiedPos);
				if (fluidState.isEmpty()) {
					return null;
				}

				// fluids stop bullets completely
				BlockHitResult fluidHit = context.getFluidShape(fluidState, level, copiedPos)
					.clip(context.getFrom(), context.getTo(), copiedPos);
				if (fluidHit != null) {
					blockHits.add(fluidHit);
					return fluidHit;
				} else {
					return null;
				}
			},
			BulletCollision::getBlockMiss
		);

		List<EntityHitResult> entityHits = getEntityCollisions(level, from, lastBlockHit.getLocation());

		if (blockHits.isEmpty() && entityHits.isEmpty()) {
			return Result.EMPTY;
		}

		List<HitResult> combinedHits = new ArrayList<>(blockHits.size() + entityHits.size());
		combinedHits.addAll(blockHits);
		combinedHits.addAll(entityHits);
		combinedHits.sort(comparingHitResultDistance(from));

		List<BulletBlockHit> blockHitResults = new ArrayList<>();
		List<BulletEntityHit> entityHitResults = new ArrayList<>();
		float damage = initialDamage;

		hitLoop:
		for (HitResult hit : combinedHits) {
			switch (hit) {
				case BlockHitResult blockHit -> {
					blockHitResults.add(BulletBlockHit.from(blockHit));

					BlockState state = blockStateCache.get(blockHit.getBlockPos());
					if (state == null) {
						break hitLoop;
					}

					damage -= initialDamage * WallbangValues.get(state);
					if (damage <= 0.0) {
						break hitLoop;
					}
				}
				case EntityHitResult entityHit -> {
					entityHitResults.add(BulletEntityHit.from(entityHit, damage));
				}
				default -> throw new AssertionError();
			}
		}

		return new Result(blockHitResults, entityHitResults);
	}

	private static BlockHitResult getBlockMiss(ClipContext clipContext) {
		Vec3 endPosition = clipContext.getTo();
		Vec3 rayDirection = clipContext.getFrom().subtract(endPosition);
		BlockPos endBlockPos = BlockPos.containing(endPosition);

		return BlockHitResult.miss(
			clipContext.getTo(),
			Direction.getApproximateNearest(rayDirection),
			endBlockPos
		);
	}

	private static List<EntityHitResult> getEntityCollisions(Level level, Vec3 from, Vec3 to) {
		List<EntityHitResult> entityHits = new ArrayList<>();

		List<Entity> nearbyEntities = level.getEntities(
			(Entity) null,
			new AABB(from, to),
			EntitySelector.LIVING_ENTITY_STILL_ALIVE
		);
		for (Entity entity : nearbyEntities) {
			Optional<Vec3> entityClip = entity.getBoundingBox().clip(from, to);

			if (entityClip.isPresent()) {
				entityHits.add(new EntityHitResult(entity, entityClip.orElseThrow()));
			}
		}

		entityHits.sort(comparingHitResultDistance(from));

		return entityHits;
	}

	private static <T extends HitResult> Comparator<T> comparingHitResultDistance(Vec3 origin) {
		return Comparator.comparingDouble(hit -> hit.getLocation().distanceToSqr(origin));
	}

	public record Result(
		List<BulletBlockHit> blockHits,
		List<BulletEntityHit> entityHits
	) {
		public static final Result EMPTY = new Result(List.of(), List.of());

		public static final StreamCodec<ByteBuf, Result> STREAM_CODEC = StreamCodec.composite(
			BulletBlockHit.STREAM_CODEC.apply(ByteBufCodecs.list()), Result::blockHits,
			BulletEntityHit.STREAM_CODEC.apply(ByteBufCodecs.list()), Result::entityHits,
			Result::new
		);
	}
}
