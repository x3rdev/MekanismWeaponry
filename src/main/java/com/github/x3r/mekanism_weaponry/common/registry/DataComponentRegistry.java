package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentRegistry {

    private static final Codec<GunItem.DataComponentCooldown> COOLDOWN_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("cooldown").forGetter(GunItem.DataComponentCooldown::cooldown),
                    Codec.LONG.fieldOf("tickOfLastShot").forGetter(GunItem.DataComponentCooldown::tickOfLastShot)
            ).apply(instance, GunItem.DataComponentCooldown::new)
    );

    private static final Codec<GunItem.DataComponentZoom> ZOOM_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.fieldOf("zoomed").forGetter(GunItem.DataComponentZoom::zoomed)
            ).apply(instance, GunItem.DataComponentZoom::new)
    );

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(MekanismWeaponry.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GunItem.DataComponentCooldown>> COOLDOWN = DATA_COMPONENTS.registerComponentType(
            "cooldown",
            builder -> builder.persistent(COOLDOWN_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GunItem.DataComponentZoom>> ZOOM = DATA_COMPONENTS.registerComponentType(
            "zoom",
            builder -> builder.persistent(ZOOM_CODEC)
    );

}
