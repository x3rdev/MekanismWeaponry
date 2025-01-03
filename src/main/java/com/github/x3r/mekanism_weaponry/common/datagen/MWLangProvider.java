package com.github.x3r.mekanism_weaponry.common.datagen;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.registry.BlockRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.ItemRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MWLangProvider extends LanguageProvider {

    public MWLangProvider(PackOutput output, String locale) {
        super(output, MekanismWeaponry.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        itemTranslations();
    }

    private void itemTranslations() {
        ItemRegistry.ITEMS.getEntries().forEach(itemDeferredHolder -> {
            add(itemDeferredHolder.get(), idToName(itemDeferredHolder.getId()));
        });
        add("mekanism_weaponry.subtitle.plasma_rifle_shoot", "Plasma rifle shot");
        add("mekanism_weaponry.subtitle.plasma_rifle_out_of_energy", "Plasma rifle out of energy");
        add("mekanism_weaponry.subtitle.railgun_shoot", "Railgun shot");
        add("mekanism_weaponry.subtitle.railgun_reload", "Railgun reloading");
        add("mekanism_weaponry.subtitle.minigun_shoot", "Minigun shot");
        add("item_group.mekanism_weaponry", "Mekanism Weaponry");

        add("mekanism_weaponry.tooltip.gun_energy", "Energy: ");
        add("mekanism_weaponry.tooltip.gun_stats", "Gun Stats");
        add("mekanism_weaponry.tooltip.gun_cooldown", "Cooldown");
        add("mekanism_weaponry.tooltip.gun_energy_usage", "Energy Usage");
        add("mekanism_weaponry.tooltip.gun_reload_time", "Reload Time");
        add("mekanism_weaponry.tooltip.gun_heat_per_shot", "Heat Per Shot");

        add("mekanism_weaponry.tooltip.gun_addons", "Gun Addons");
        add("mekanism_weaponry.tooltip.gun_no_addons", "No Addons Installed");



        add("mekanism_weaponry.tooltip.addon_effect_multiplier", "Effect multiplier");

    }

    private String idToName(ResourceLocation location) {
        String[] tokens = location.getPath().split("_");
        StringBuilder result = new StringBuilder();
        for (String token : tokens) {
            char c = Character.toUpperCase(token.charAt(0));
            result.append(c).append(token.substring(1)).append(" ");
        }
        return result.toString().trim();
    }
}
