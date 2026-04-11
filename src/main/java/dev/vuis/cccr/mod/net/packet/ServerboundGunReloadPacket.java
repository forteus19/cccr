package dev.vuis.cccr.mod.net.packet;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import dev.vuis.cccr.mod.CounterCraft;
import dev.vuis.cccr.mod.data.Ammo;
import dev.vuis.cccr.mod.item.GunItem;

public final class ServerboundGunReloadPacket implements CustomPacketPayload {
    public static final ServerboundGunReloadPacket INSTANCE = new ServerboundGunReloadPacket();

    public static final Type<ServerboundGunReloadPacket> TYPE = new Type<>(CounterCraft.id("gun_reload"));
    public static final StreamCodec<ByteBuf, ServerboundGunReloadPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ServerboundGunReloadPacket() {
    }

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

        int reloadTicks = GunItem.getReloadTicks(heldStack);
        if (reloadTicks > 0) {
            return;
        }

        Ammo ammo = GunItem.getAmmo(heldStack);
        if (ammo.loaded() >= gunItem.maxAmmo.loaded() || ammo.reserve() == 0) {
            return;
        }

        GunItem.setReloadTicks(heldStack, gunItem.maxReloadTicks);

        int usedAmmo = gunItem.maxAmmo.loaded() - ammo.loaded();
        GunItem.setAmmo(heldStack, new Ammo(
            Math.min(ammo.loaded() + ammo.reserve(), gunItem.maxAmmo.loaded()),
            Math.max(ammo.reserve() - usedAmmo, 0)
        ));

        level.playSound(
            null, player,
            gunItem.soundReload.get(),
            SoundSource.PLAYERS,
            1f, 1f
        );
    }
}
