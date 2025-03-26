package com.github.x3r.mekanism_weaponry.common.packet;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class MekanismWeaponryPacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MekanismWeaponry.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        int id = 0;
        INSTANCE.registerMessage(id++, ActivateGunClientPacket.class, ActivateGunClientPacket::encode, ActivateGunClientPacket::decode, ActivateGunClientPacket::receivePacket);
        INSTANCE.registerMessage(id++, ActivateGunServerPacket.class, ActivateGunServerPacket::encode, ActivateGunServerPacket::decode, ActivateGunServerPacket::receivePacket);
        INSTANCE.registerMessage(id++, DeactivateGunPacket.class, DeactivateGunPacket::encode, DeactivateGunPacket::decode, DeactivateGunPacket::receivePacket);
        INSTANCE.registerMessage(id++, ReloadGunClientPacket.class, ReloadGunClientPacket::encode, ReloadGunClientPacket::decode, ReloadGunClientPacket::receivePacket);
        INSTANCE.registerMessage(id++, ReloadGunServerPacket.class, ReloadGunServerPacket::encode, ReloadGunServerPacket::decode, ReloadGunServerPacket::receivePacket);
    }

    public static void sendToClient(Object msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }


    public static void sendToServer(Object msg) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);
    }

}
