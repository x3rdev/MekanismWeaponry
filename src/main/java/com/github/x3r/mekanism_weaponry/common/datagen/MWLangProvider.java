package com.github.x3r.mekanism_weaponry.common.datagen;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;

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
        add("mekanism_weaponry.subtitle.gun_out_of_energy", "Gun out of energy");
        add("mekanism_weaponry.subtitle.railgun_shoot", "Railgun shot");
        add("mekanism_weaponry.subtitle.railgun_reload", "Railgun reloading");
        add("mekanism_weaponry.subtitle.minigun_shoot", "Minigun shot");
        add("mekanism_weaponry.subtitle.gun_out_of_ammo", "Gun out of ammo");

        add("item_group.mekanism_weaponry", "Mekanism Weaponry");

        add("mekanism_weaponry.tooltip.gun_energy", "Energy: ");
        add("mekanism_weaponry.tooltip.gun_stats", "Gun Stats");
        add("mekanism_weaponry.tooltip.gun_cooldown", "Cooldown");
        add("mekanism_weaponry.tooltip.gun_energy_usage", "Energy Usage");
        add("mekanism_weaponry.tooltip.gun_reload_time", "Reload Time");
        add("mekanism_weaponry.tooltip.gun_heat_per_shot", "Heat Per Shot");
        add("mekanism_weaponry.tooltip.gun_loaded_ammo", "Loaded Ammo: ");

        add("mekanism_weaponry.tooltip.gun_addons", "Gun Addons");
        add("mekanism_weaponry.tooltip.gun_no_addons", "No Addons Installed");

        add("mekanism_weaponry.tooltip.addon_effect_multiplier", "Effect multiplier");

        add("mekanism_weaponry.tooltip.color_desc.nimbus", "Classic blue and white");
        add("mekanism_weaponry.tooltip.color_desc.alien", "Out of this world");
        add("mekanism_weaponry.tooltip.color_desc.cotton_candy", "Sugar tooth");
        add("mekanism_weaponry.tooltip.color_desc.eva", "It's not a robot");
        add("mekanism_weaponry.tooltip.color_desc.bumblebee", "Float like a butterfly");
        add("mekanism_weaponry.tooltip.color_desc.crimson", "Blood red");

        add("mekanism_weaponry.tooltip.energy_usage_chip", "Reduces the energy cost of the weapon");
        add("mekanism_weaponry.tooltip.fire_rate_chip", "Reduces the time between shot of the weapon");
        add("mekanism_weaponry.tooltip.heat_per_shot_chip", "Reduces the heat accumulated when the weapon is shot");
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
