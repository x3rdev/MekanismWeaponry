package com.github.x3r.mekanism_weaponry;

import com.github.x3r.mekanism_weaponry.common.packet.MekanismWeaponryPacketHandler;
import com.github.x3r.mekanism_weaponry.common.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MekanismWeaponry.MOD_ID)
public class MekanismWeaponry {

    public static final String MOD_ID = "mekanism_weaponry";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MekanismWeaponry() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        BlockRegistry.BLOCKS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        ItemRegistry.ModItemTab.CREATIVE_MODE_TABS.register(modEventBus);
        MenuTypeRegistry.MENU_TYPES.register(modEventBus);
        ParticleRegistry.PARTICLE_TYPES.register(modEventBus);
        SoundRegistry.SOUND_EVENTS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MekanismWeaponryConfig.CONFIG_SPEC);

        MekanismWeaponryPacketHandler.registerPackets();

    }

}
