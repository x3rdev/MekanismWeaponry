package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MekanismWeaponry.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PLASMA_RIFLE_SHOOT = registerSound("plasma_rifle_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUN_OUT_OF_ENERGY = registerSound("gun_out_of_energy");
    public static final DeferredHolder<SoundEvent, SoundEvent> RAILGUN_SHOOT = registerSound("railgun_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> RAILGUN_RELOAD = registerSound("railgun_reload");
    public static final DeferredHolder<SoundEvent, SoundEvent> MINIGUN_SHOOT = registerSound("minigun_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUN_OUT_OF_AMMO = registerSound("gun_out_of_ammo");


    public static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, name)));
    }
}
