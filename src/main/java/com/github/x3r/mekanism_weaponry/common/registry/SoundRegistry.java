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
    public static final DeferredHolder<SoundEvent, SoundEvent> PLASMA_RIFLE_OUT_OF_ENERGY = registerSound("plasma_rifle_out_of_energy");

    public static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, name)));
    }
}
