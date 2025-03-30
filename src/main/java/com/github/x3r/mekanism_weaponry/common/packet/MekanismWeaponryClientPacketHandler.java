package com.github.x3r.mekanism_weaponry.common.packet;

import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class MekanismWeaponryClientPacketHandler {

    public static void activateGunClientPacket(Supplier<NetworkEvent.Context> context) {
        ItemStack stack = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND);
        if(stack.getItem() instanceof GunItem item) {
            item.clientShoot(stack, Minecraft.getInstance().player);
        }
    }

    public static void reloadGunClientPacket(Supplier<NetworkEvent.Context> context) {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(stack.getItem() instanceof GunItem item) {
            item.clientReload(stack, player);
        }
    }
}
