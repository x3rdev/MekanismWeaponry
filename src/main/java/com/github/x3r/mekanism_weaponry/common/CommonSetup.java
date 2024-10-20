package com.github.x3r.mekanism_weaponry.common;

import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.github.x3r.mekanism_weaponry.common.packet.ActivateGunPayload;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.ItemRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.common.attachments.containers.energy.EnergyContainersBuilder;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class CommonSetup {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new ComponentEnergyStorage(stack, DataComponentRegistry.ENERGY.get(), 10000, 1000, 1000),
                ItemRegistry.PLASMA_RIFLE.get());
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new ComponentEnergyStorage(stack, DataComponentRegistry.ENERGY.get(), 10000, 1000, 1000),
                ItemRegistry.GAUNTLET.get());
    }
}
