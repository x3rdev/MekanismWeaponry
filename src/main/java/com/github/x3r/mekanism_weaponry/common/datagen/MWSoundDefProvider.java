package com.github.x3r.mekanism_weaponry.common.datagen;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class MWSoundDefProvider extends SoundDefinitionsProvider {

    protected MWSoundDefProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, MekanismWeaponry.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(SoundRegistry.PLASMA_RIFLE_SHOOT.get(), definition()
                .subtitle("mekanism_weaponry.subtitle.plasma_rifle_shoot")
                .with(SoundDefinition.Sound.sound(
                        new ResourceLocation(MekanismWeaponry.MOD_ID, "item/plasma_rifle_shoot"),
                        SoundDefinition.SoundType.SOUND)));
        add(SoundRegistry.GUN_OUT_OF_ENERGY.get(), definition()
                .subtitle("mekanism_weaponry.subtitle.gun_out_of_energy")
                .with(SoundDefinition.Sound.sound(
                        new ResourceLocation(MekanismWeaponry.MOD_ID, "item/gun_out_of_energy"),
                        SoundDefinition.SoundType.SOUND)));
        add(SoundRegistry.RAILGUN_SHOOT.get(), definition()
                .subtitle("mekanism_weaponry.subtitle.railgun_shoot")
                .with(SoundDefinition.Sound.sound(
                        new ResourceLocation(MekanismWeaponry.MOD_ID, "item/railgun_shoot"),
                        SoundDefinition.SoundType.SOUND)));
        add(SoundRegistry.RAILGUN_RELOAD.get(), definition()
                .subtitle("mekanism_weaponry.subtitle.railgun_reload")
                .with(SoundDefinition.Sound.sound(
                        new ResourceLocation(MekanismWeaponry.MOD_ID, "item/railgun_reload"),
                        SoundDefinition.SoundType.SOUND)));
        add(SoundRegistry.MINIGUN_SHOOT.get(), definition()
                .subtitle("mekanism_weaponry.subtitle.minigun_shoot")
                .with(SoundDefinition.Sound.sound(
                        new ResourceLocation(MekanismWeaponry.MOD_ID, "item/minigun_shoot"),
                        SoundDefinition.SoundType.SOUND)));
        add(SoundRegistry.GUN_OUT_OF_AMMO.get(), definition()
                .subtitle("mekanism_weaponry.subtitle.gun_out_of_ammo")
                .with(SoundDefinition.Sound.sound(
                        new ResourceLocation(MekanismWeaponry.MOD_ID, "item/gun_out_of_ammo"),
                        SoundDefinition.SoundType.SOUND)));
    }
}
