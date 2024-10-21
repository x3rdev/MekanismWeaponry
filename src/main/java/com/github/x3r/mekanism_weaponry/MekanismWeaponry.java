package com.github.x3r.mekanism_weaponry;

import com.github.x3r.mekanism_weaponry.client.ClientSetup;
import com.github.x3r.mekanism_weaponry.common.CommonSetup;
import com.github.x3r.mekanism_weaponry.common.packet.MekanismWeaponryPacketHandler;
import com.github.x3r.mekanism_weaponry.common.recipe.AnvilRecipes;
import com.github.x3r.mekanism_weaponry.common.registry.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(MekanismWeaponry.MOD_ID)
public class MekanismWeaponry {

    public static final String MOD_ID = "mekanism_weaponry";

    public MekanismWeaponry(IEventBus modEventBus, ModContainer modContainer) {
        IEventBus neoEventBus = NeoForge.EVENT_BUS;

        BlockRegistry.BLOCKS.register(modEventBus);
        DataComponentRegistry.DATA_COMPONENTS.register(modEventBus);
//        BlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);
//        ContainerTypeRegistry.CONTAINER_TYPES.register(modEventBus);

        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        ItemRegistry.ModItemTab.CREATIVE_MODE_TABS.register(modEventBus);
        SoundRegistry.SOUND_EVENTS.register(modEventBus);

        modEventBus.addListener(MekanismWeaponryPacketHandler::registerPayloadHandler);
        modEventBus.addListener(CommonSetup::registerCapabilities);
        neoEventBus.addListener(AnvilRecipes::anvilUpdate);

        neoEventBus.addListener(ClientSetup::pressKey);
        neoEventBus.addListener(ClientSetup::onClientTick);
        neoEventBus.addListener(ClientSetup::cameraSetupEvent);


    }
}
