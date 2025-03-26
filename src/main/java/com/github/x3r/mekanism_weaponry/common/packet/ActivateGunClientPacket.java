package com.github.x3r.mekanism_weaponry.common.packet;

import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ActivateGunClientPacket  {

    public ActivateGunClientPacket() {

    }

    public void encode(FriendlyByteBuf buffer) {

    }

    public static ActivateGunClientPacket decode(FriendlyByteBuf buffer) {
        return new ActivateGunClientPacket();
    }

    public void receivePacket(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(stack.getItem() instanceof GunItem item) {
                item.clientShoot(stack, player);
            }
        });
        context.get().setPacketHandled(true);
    }

}
