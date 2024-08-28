package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.GauntletItem;
import com.github.x3r.mekanism_weaponry.common.item.PlasmaRifleItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MekanismWeaponry.MOD_ID);

    public static final DeferredHolder<Item, Item> GAUNTLET = ITEMS.register("gauntlet",
            () -> new GauntletItem(new Item.Properties()));

    public static final DeferredHolder<Item, Item> PLASMA_RIFLE = ITEMS.register("plasma_rifle",
            () -> new PlasmaRifleItem(new Item.Properties()));


    public static class ModItemTab {

        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MekanismWeaponry.MOD_ID);

        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MEKT_ITEM_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                .icon(() -> Items.ANCIENT_DEBRIS.asItem().getDefaultInstance())
                .title(Component.translatable("item_group." + MekanismWeaponry.MOD_ID))
                .displayItems((displayParameters, output) -> {
                    ItemRegistry.ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(itemRegistryObject.get()));
                    BlockRegistry.BLOCKS.getPrimaryEntries().forEach(iBlockProvider -> output.accept(iBlockProvider.get().asItem()));
                })
                .build());
    }
}
