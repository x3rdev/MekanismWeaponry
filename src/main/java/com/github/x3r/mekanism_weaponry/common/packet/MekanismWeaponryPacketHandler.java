package com.github.x3r.mekanism_weaponry.common.packet;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class MekanismWeaponryPacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playBidirectional(
                ActivateGunPayload.TYPE,
                ActivateGunPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ActivateGunPayload::handleClient,
                        ActivateGunPayload::handleServer
                )

        );
        registrar.playBidirectional(
                ReloadGunPayload.TYPE,
                ReloadGunPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ReloadGunPayload::handleClient,
                        ReloadGunPayload::handleServer
                )
        );
    }
}
