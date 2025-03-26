package com.github.x3r.mekanism_weaponry.common.packet;

import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.function.Supplier;

public class ActivateGunServerPacket  {

    public ActivateGunServerPacket() {

    }

    public void encode(FriendlyByteBuf buffer) {

    }

    public static ActivateGunServerPacket decode(FriendlyByteBuf buffer) {
        return new ActivateGunServerPacket();
    }

    public void receivePacket(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            GeoItem.getOrAssignId(stack, player.serverLevel());
            if(stack.getItem() instanceof GunItem item) {
                item.serverShoot(stack, player);
            }
        });
        context.get().setPacketHandled(true);
    }

}
