package dev.vuis.cccr.mod.net.packet;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.data.Ammo;
import dev.vuis.cccr.mod.item.GunItem;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntFloatPair;
import java.util.List;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public record ServerboundGunShootPacket(
	Vec3 from,
	Vec3 to,
	List<Pair<Vec3, BlockPos>> blockHits,
	List<IntFloatPair> entityHits // (entity id, damage)
) implements CustomPacketPayload {
	public static final Type<ServerboundGunShootPacket> TYPE = new Type<>(CounterCraft.id("gun_shoot"));

	private static final StreamCodec<ByteBuf, Pair<Vec3, BlockPos>> BLOCK_HIT_STREAM_CODEC = StreamCodec.composite(
		Vec3.STREAM_CODEC, Pair::left,
		BlockPos.STREAM_CODEC, Pair::right,
		Pair::of
	);
	private static final StreamCodec<ByteBuf, IntFloatPair> ENTITY_HIT_STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, IntFloatPair::leftInt,
		ByteBufCodecs.FLOAT, IntFloatPair::rightFloat,
		IntFloatPair::of
	);
	public static final StreamCodec<ByteBuf, ServerboundGunShootPacket> STREAM_CODEC = StreamCodec.composite(
		Vec3.STREAM_CODEC, ServerboundGunShootPacket::from,
		Vec3.STREAM_CODEC, ServerboundGunShootPacket::to,
		BLOCK_HIT_STREAM_CODEC.apply(ByteBufCodecs.list()), ServerboundGunShootPacket::blockHits,
		ENTITY_HIT_STREAM_CODEC.apply(ByteBufCodecs.list()), ServerboundGunShootPacket::entityHits,
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

		for (Pair<Vec3, BlockPos> blockHit : blockHits) {
			Vec3 location = blockHit.left();
			BlockState hitState = level.getBlockState(blockHit.right());

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

		for (IntFloatPair entityHit : entityHits) {
			Entity entity = level.getEntity(entityHit.leftInt());
			if (!(entity instanceof LivingEntity livingEntity)) {
				continue;
			}

			livingEntity.hurtTime = 0;
			livingEntity.invulnerableTime = 0;
			if (livingEntity.hurtServer(level, level.damageSources().playerAttack(player), entityHit.rightFloat())) {
				livingEntity.setDeltaMovement(0.0, 0.0, 0.0);
			}
		}
	}
}
