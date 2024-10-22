package com.github.x3r.mekanism_weaponry.common.packet;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ReloadGunPayload() implements CustomPacketPayload {
    public static final Type<ReloadGunPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "reload_gun"));
    public static final StreamCodec<ByteBuf, ReloadGunPayload> STREAM_CODEC = StreamCodec.unit(new ReloadGunPayload());
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleClient(final ReloadGunPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            LocalPlayer player = (LocalPlayer) context.player();
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(stack.getItem() instanceof GunItem item) {
                item.clientReload(stack, item, player);
            }
        });
    }

    public static void handleServer(final ReloadGunPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = ((ServerPlayer) context.player());
            ItemStack stack = player.getMainHandItem();
            if(stack.getItem() instanceof GunItem item) {
                item.tryStartReload(stack, player);
            }
        });
    }
}
