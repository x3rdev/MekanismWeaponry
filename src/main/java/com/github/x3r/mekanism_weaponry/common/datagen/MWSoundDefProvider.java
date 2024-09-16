package com.github.x3r.mekanism_weaponry.common.datagen;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class MWSoundDefProvider extends SoundDefinitionsProvider {

    protected MWSoundDefProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, MekanismWeaponry.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(SoundRegistry.LASER_RIFLE_SHOOT.get(), definition()
                .subtitle("mekanism_weaponry.subtitle.plasma_rifle_shoot")
                .with(SoundDefinition.Sound.sound(
                        ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "item/plasma_rifle_shoot"),
                        SoundDefinition.SoundType.SOUND)));
    }
}