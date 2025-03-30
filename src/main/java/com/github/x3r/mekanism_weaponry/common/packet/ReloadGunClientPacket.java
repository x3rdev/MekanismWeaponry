package com.github.x3r.mekanism_weaponry.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ReloadGunClientPacket {

    public ReloadGunClientPacket() {

    }

    public void encode(FriendlyByteBuf buffer) {

    }

    public static ReloadGunClientPacket decode(FriendlyByteBuf buffer) {
        return new ReloadGunClientPacket();
    }

    public void receivePacket(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> MekanismWeaponryClientPacketHandler.reloadGunClientPacket(context));
        });
    }
}
