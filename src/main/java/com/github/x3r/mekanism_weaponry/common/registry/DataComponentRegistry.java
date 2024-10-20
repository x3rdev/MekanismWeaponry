package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentRegistry {

    private static final Codec<GunItem.DataComponentChips> CHIPS_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(ItemStack.CODEC).fieldOf("chips").forGetter(GunItem.DataComponentChips::chips)
            ).apply(instance, GunItem.DataComponentChips::new)
    );

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MekanismWeaponry.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> COOLDOWN = DATA_COMPONENTS.registerComponentType(
            "cooldown",
            builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GunItem.DataComponentChips>> CHIPS = DATA_COMPONENTS.registerComponentType(
            "chips",
            builder -> builder.persistent(CHIPS_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENERGY = DATA_COMPONENTS.registerComponentType(
            "energy",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );

}
