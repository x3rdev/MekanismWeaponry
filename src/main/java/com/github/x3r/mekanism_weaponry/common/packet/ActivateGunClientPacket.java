package com.github.x3r.mekanism_weaponry.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
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
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> MekanismWeaponryClientPacketHandler.activateGunClientPacket(context));
        });
        context.get().setPacketHandled(true);
    }

}
