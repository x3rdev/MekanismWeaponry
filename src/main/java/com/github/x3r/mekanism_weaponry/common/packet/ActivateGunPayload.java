package com.github.x3r.mekanism_weaponry.common.packet;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ActivateGunPayload() implements CustomPacketPayload {
    public static final Type<ActivateGunPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "activate_gun"));
    public static final StreamCodec<ByteBuf, ActivateGunPayload> STREAM_CODEC = StreamCodec.unit(new ActivateGunPayload());
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServer(final ActivateGunPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(stack.getItem() instanceof GunItem item && item.isGunReady(stack, player.serverLevel().getGameTime())) {
                item.serverShoot(stack, item, player);
            }
        });
    }
}
