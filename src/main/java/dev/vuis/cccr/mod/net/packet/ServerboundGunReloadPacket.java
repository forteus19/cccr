package dev.vuis.cccr.mod.net.packet;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import dev.vuis.cccr.mod.CounterCraft;
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

        ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(heldStack.getItem() instanceof GunItem gunItem)) {
            return;
        }

        gunItem.reload(player.level(), player, heldStack);
    }
}
