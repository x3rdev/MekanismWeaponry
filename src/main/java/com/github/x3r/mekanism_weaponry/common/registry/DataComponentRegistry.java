package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentRegistry {

    private static final Codec<GunItem.DataComponentAddons> ADDONS_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("chip1").forGetter(o -> o.getAddon(0)),
                    ItemStack.CODEC.fieldOf("paint").forGetter(o -> o.getAddon(1)),
                    ItemStack.CODEC.fieldOf("chip2").forGetter(o -> o.getAddon(2)),
                    ItemStack.CODEC.fieldOf("scope").forGetter(o -> o.getAddon(3)),
                    ItemStack.CODEC.fieldOf("chip3").forGetter(o -> o.getAddon(4))
            ).apply(instance, GunItem.DataComponentAddons::new)
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, GunItem.DataComponentAddons> ADDONS_NETWORK_CODEC = new StreamCodec<>() {
        @Override
        public GunItem.DataComponentAddons decode(RegistryFriendlyByteBuf buffer) {
            GunItem.DataComponentAddons addons = new GunItem.DataComponentAddons();
            for (int i = 0; i < 4; i++) {
                addons.setAddon(ItemStack.STREAM_CODEC.decode(buffer), i);
            }
            return addons;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, GunItem.DataComponentAddons value) {
            for (int i = 0; i < 4; i++) {
                ItemStack.STREAM_CODEC.encode(buffer, value.getAddon(i));
            }
        }
    };


    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MekanismWeaponry.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> LAST_SHOT_TICK = DATA_COMPONENTS.registerComponentType(
            "last_shot_tick",
            builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GunItem.DataComponentAddons>> ADDONS = DATA_COMPONENTS.registerComponentType(
            "addons",
            builder -> builder.persistent(ADDONS_CODEC).networkSynchronized(ADDONS_NETWORK_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENERGY = DATA_COMPONENTS.registerComponentType(
            "energy",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> HEAT = DATA_COMPONENTS.registerComponentType(
            "heat",
            builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> RELOADING = DATA_COMPONENTS.registerComponentType(
            "reloading",
            builder -> builder.persistent(Codec.BOOL)
    );

}
