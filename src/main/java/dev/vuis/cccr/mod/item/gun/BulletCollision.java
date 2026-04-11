package dev.vuis.cccr.mod.item.gun;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.phys.Vec3;

public final class BulletCollision {
    private BulletCollision() {
        throw new AssertionError();
    }

    public static BulletHitResult bullet(Level level, ClipContext clipContext) {
        BlockHitResult blockCollision = getBlockCollision(level, clipContext);
        EntityHitResult entityCollision = getEntityCollision(level, clipContext.getFrom(), blockCollision.getLocation());
        return new BulletHitResult(blockCollision, entityCollision);
    }

    private static BlockHitResult getBlockCollision(BlockGetter levelView, ClipContext clipContext) {
        return BlockGetter.traverseBlocks(
            clipContext.getFrom(), clipContext.getTo(), clipContext,
            (context, pos) -> clipTraversedBlockPos(levelView, context, pos),
            BulletCollision::getBlockMiss
        );
    }

    private static BlockHitResult clipTraversedBlockPos(BlockGetter levelView, ClipContext clipContext, BlockPos pos) {
        BlockState blockState = levelView.getBlockState(pos);
        if (blockState.isAir()) {
            return null;
        }

        BlockHitResult blockHit = clipContext.getBlockShape(blockState, levelView, pos)
            .clip(clipContext.getFrom(), clipContext.getTo(), pos);
        if (blockHit != null) {
            return blockHit;
        }

        FluidState fluidState = levelView.getFluidState(pos);
        if (fluidState.isEmpty()) {
            return null;
        }
        return clipContext.getFluidShape(fluidState, levelView, pos)
            .clip(clipContext.getFrom(), clipContext.getTo(), pos);
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

    private static EntityHitResult getEntityCollision(Level level, Vec3 from, Vec3 to) {
        List<EntityHitResult> potentialEntityHits = new ArrayList<>();

        List<Entity> nearbyEntities = level.getEntities(
            (Entity) null,
            new AABB(from, to),
            EntitySelector.LIVING_ENTITY_STILL_ALIVE
        );
        for (Entity entity : nearbyEntities) {
            Optional<Vec3> entityClip = entity.getBoundingBox().clip(from, to);

            if (entityClip.isPresent()) {
                potentialEntityHits.add(new EntityHitResult(entity, entityClip.orElseThrow()));
            }
        }

        potentialEntityHits.sort(Comparator.comparingDouble(hit -> hit.getLocation().distanceToSqr(from)));

        return potentialEntityHits.isEmpty() ? null : potentialEntityHits.getFirst();
    }
}
