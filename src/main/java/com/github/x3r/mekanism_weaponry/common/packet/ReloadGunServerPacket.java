package com.github.x3r.mekanism_weaponry.common.packet;

import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ReloadGunServerPacket {

    public ReloadGunServerPacket() {

    }

    public void encode(FriendlyByteBuf buffer) {

    }

    public static ReloadGunServerPacket decode(FriendlyByteBuf buffer) {
        return new ReloadGunServerPacket();
    }

    public void receivePacket(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            ItemStack stack = player.getMainHandItem();
            if(stack.getItem() instanceof GunItem item) {
                item.tryStartReload(stack, player);
            }
        });
        context.get().setPacketHandled(true);
    }
}
