package com.github.x3r.mekanism_weaponry.common.datagen;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.registry.BlockRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.ItemRegistry;
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
