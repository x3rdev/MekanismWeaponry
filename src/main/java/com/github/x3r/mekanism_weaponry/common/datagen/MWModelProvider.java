package com.github.x3r.mekanism_weaponry.common.datagen;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.addon.GunAddonItem;
import com.github.x3r.mekanism_weaponry.common.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MWModelProvider extends ItemModelProvider {

    public MWModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MekanismWeaponry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ItemRegistry.ITEMS.getEntries().forEach(itemDeferredHolder -> {
            if(itemDeferredHolder.get() instanceof GunAddonItem item) {
                if(item.getAddonType().equals(GunAddonItem.AddonType.CHIP)) {
                    getBuilder(item.toString())
                            .parent(new ModelFile.UncheckedModelFile(MekanismWeaponry.MOD_ID + ":item/gun_chip"));
                }
                if(item.getAddonType().equals(GunAddonItem.AddonType.PAINT)) {
                    getBuilder(item.toString())
                            .parent(new ModelFile.UncheckedModelFile(MekanismWeaponry.MOD_ID + ":item/paint_bucket"));
                }
            }
        });
    }
}
