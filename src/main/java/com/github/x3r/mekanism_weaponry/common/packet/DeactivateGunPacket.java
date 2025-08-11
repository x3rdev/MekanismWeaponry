package com.github.x3r.mekanism_weaponry.common.packet;

import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DeactivateGunPacket {

    public DeactivateGunPacket() {

    }

    public void encode(FriendlyByteBuf buffer) {

    }

    public static DeactivateGunPacket decode(FriendlyByteBuf buffer) {
        return new DeactivateGunPacket();
    }

    public void receivePacket(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(stack.getItem() instanceof GunItem item) {
                item.serverStoppedShooting(stack);
            }
        });
        context.get().setPacketHandled(true);
    }
}
