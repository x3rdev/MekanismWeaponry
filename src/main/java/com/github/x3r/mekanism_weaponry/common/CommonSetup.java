package com.github.x3r.mekanism_weaponry.common;

import com.github.x3r.mekanism_weaponry.MekanismWeaponryConfig;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.ItemRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;

public class CommonSetup {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new ComponentEnergyStorage(stack, DataComponentRegistry.ENERGY.get(),
                        MekanismWeaponryConfig.CONFIG.getPlasmaRifleEnergyCapacity(),
                        MekanismWeaponryConfig.CONFIG.getPlasmaRifleEnergyTransfer()),
                ItemRegistry.PLASMA_RIFLE.get());
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new ComponentEnergyStorage(stack, DataComponentRegistry.ENERGY.get(),
                        MekanismWeaponryConfig.CONFIG.getRailgunEnergyCapacity(),
                        MekanismWeaponryConfig.CONFIG.getRailgunEnergyTransfer()),
                ItemRegistry.RAILGUN.get());
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new ComponentEnergyStorage(stack, DataComponentRegistry.ENERGY.get(),
                        MekanismWeaponryConfig.CONFIG.getTeslaMinigunEnergyCapacity(),
                        MekanismWeaponryConfig.CONFIG.getTeslaMinigunEnergyTransfer()),
                ItemRegistry.TESLA_MINIGUN.get());
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new ComponentEnergyStorage(stack, DataComponentRegistry.ENERGY.get(),
                        MekanismWeaponryConfig.CONFIG.getGauntletEnergyCapacity(),
                        MekanismWeaponryConfig.CONFIG.getGauntletEnergyTransfer()),
                ItemRegistry.GAUNTLET.get());
    }

}
