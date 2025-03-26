package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MekanismWeaponry.MOD_ID);

    public static final RegistryObject<SoundEvent> PLASMA_RIFLE_SHOOT = registerSound("plasma_rifle_shoot");
    public static final RegistryObject<SoundEvent> GUN_OUT_OF_ENERGY = registerSound("gun_out_of_energy");
    public static final RegistryObject<SoundEvent> RAILGUN_SHOOT = registerSound("railgun_shoot");
    public static final RegistryObject<SoundEvent> RAILGUN_RELOAD = registerSound("railgun_reload");
    public static final RegistryObject<SoundEvent> MINIGUN_SHOOT = registerSound("minigun_shoot");
    public static final RegistryObject<SoundEvent> GUN_OUT_OF_AMMO = registerSound("gun_out_of_ammo");


    public static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MekanismWeaponry.MOD_ID, name)));
    }
}
