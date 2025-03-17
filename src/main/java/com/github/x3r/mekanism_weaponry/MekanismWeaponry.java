package com.github.x3r.mekanism_weaponry;

import com.github.x3r.mekanism_weaponry.client.ClientSetup;
import com.github.x3r.mekanism_weaponry.common.CommonSetup;
import com.github.x3r.mekanism_weaponry.common.packet.MekanismWeaponryPacketHandler;
import com.github.x3r.mekanism_weaponry.common.registry.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Mod(MekanismWeaponry.MOD_ID)
public class MekanismWeaponry {

    public static final String MOD_ID = "mekanism_weaponry";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MekanismWeaponry(IEventBus modEventBus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.COMMON, MekanismWeaponryConfig.CONFIG_SPEC);

        BlockRegistry.BLOCKS.register(modEventBus);
        DataComponentRegistry.DATA_COMPONENTS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        ItemRegistry.ModItemTab.CREATIVE_MODE_TABS.register(modEventBus);
        MenuTypeRegistry.MENU_TYPES.register(modEventBus);
        ParticleRegistry.PARTICLE_TYPES.register(modEventBus);
        SoundRegistry.SOUND_EVENTS.register(modEventBus);

        modEventBus.addListener(MekanismWeaponryPacketHandler::registerPayloadHandler);
        modEventBus.addListener(CommonSetup::registerCapabilities);



    }

}
