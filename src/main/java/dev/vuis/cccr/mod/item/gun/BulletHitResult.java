package dev.vuis.cccr.mod.item.gun;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record BulletHitResult(
    @NonNull BlockHitResult block,
    @Nullable EntityHitResult entity
) {
}
