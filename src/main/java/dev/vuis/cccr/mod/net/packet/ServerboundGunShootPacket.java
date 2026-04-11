package dev.vuis.cccr.mod.net.packet;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.data.Ammo;
import dev.vuis.cccr.mod.item.GunItem;
import dev.vuis.cccr.mod.item.gun.BulletCollision;
import dev.vuis.cccr.mod.item.gun.BulletHitResult;

public record ServerboundGunShootPacket(Vec3 from, Vec3 to) implements CustomPacketPayload {
    public static final Type<ServerboundGunShootPacket> TYPE = new Type<>(CounterCraft.id("gun_shoot"));

    public static final StreamCodec<ByteBuf, ServerboundGunShootPacket> STREAM_CODEC = StreamCodec.composite(
        Vec3.STREAM_CODEC, ServerboundGunShootPacket::from,
        Vec3.STREAM_CODEC, ServerboundGunShootPacket::to,
        ServerboundGunShootPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        ServerLevel level = player.level();

        ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);
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

        BulletHitResult hitResult = BulletCollision.bullet(
            level, new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, player)
        );

        BlockHitResult blockHit = hitResult.block();

        if (blockHit.getType() == HitResult.Type.BLOCK) {
            Vec3 location = blockHit.getLocation();
            BlockPos hitPos = blockHit.getBlockPos();
            BlockState hitState = level.getBlockState(hitPos);

            if (!hitState.isAir()) {
                level.playSound(
                    player,
                    location.x, location.y, location.z,
                    hitState.getSoundType().getBreakSound(), SoundSource.BLOCKS
                );
                level.sendParticles(
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
            if (entity.hurtServer(level, level.damageSources().playerAttack(player), gunItem.damage)) {
                entity.setDeltaMovement(0.0, 0.0, 0.0);
            }
        }
    }
}
