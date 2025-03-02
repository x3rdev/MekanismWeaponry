package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.*;
import com.github.x3r.mekanism_weaponry.common.item.addon.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MekanismWeaponry.MOD_ID);

    public static final DeferredHolder<Item, GauntletItem> GAUNTLET = ITEMS.register("gauntlet",
            () -> new GauntletItem(new Item.Properties()));

    public static final DeferredHolder<Item, PlasmaRifleItem> PLASMA_RIFLE = ITEMS.register("plasma_rifle",
            () -> new PlasmaRifleItem(new Item.Properties()));

    public static final DeferredHolder<Item, RailgunItem> RAILGUN = ITEMS.register("railgun",
            () -> new RailgunItem(new Item.Properties()));

    public static final DeferredHolder<Item, TeslaMinigunItem> TESLA_MINIGUN = ITEMS.register("tesla_minigun",
            () -> new TeslaMinigunItem(new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> WEAPON_WORKBENCH = ITEMS.register("weapon_workbench",
            () -> new BlockItem(BlockRegistry.WEAPON_WORKBENCH.get(), new Item.Properties()));

    public static final DeferredHolder<Item, Item> STEEL_ROD = ITEMS.register("steel_rod",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> PLASMA_CIRCUIT = ITEMS.register("plasma_circuit",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> MAGNETIC_CIRCUIT = ITEMS.register("magnetic_circuit",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> FIRE_RATE_CHIP_1 = ITEMS.register("fire_rate_chip_1",
            () -> new FireRateChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 1.0F));
    public static final DeferredHolder<Item, Item> FIRE_RATE_CHIP_2 = ITEMS.register("fire_rate_chip_2",
            () -> new FireRateChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 2.0F));
    public static final DeferredHolder<Item, Item> FIRE_RATE_CHIP_3 = ITEMS.register("fire_rate_chip_3",
            () -> new FireRateChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 3.0F));
    public static final DeferredHolder<Item, Item> FIRE_RATE_CHIP_4 = ITEMS.register("fire_rate_chip_4",
            () -> new FireRateChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 4.0F));

    public static final DeferredHolder<Item, Item> ENERGY_USAGE_CHIP_1 = ITEMS.register("energy_usage_chip_1",
            () -> new EnergyUsageChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 1.0F));
    public static final DeferredHolder<Item, Item> ENERGY_USAGE_CHIP_2 = ITEMS.register("energy_usage_chip_2",
            () -> new EnergyUsageChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 2.0F));
    public static final DeferredHolder<Item, Item> ENERGY_USAGE_CHIP_3 = ITEMS.register("energy_usage_chip_3",
            () -> new EnergyUsageChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 3.0F));
    public static final DeferredHolder<Item, Item> ENERGY_USAGE_CHIP_4 = ITEMS.register("energy_usage_chip_4",
            () -> new EnergyUsageChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 4.0F));

    public static final DeferredHolder<Item, Item> HEAT_PER_SHOT_CHIP_1 = ITEMS.register("heat_per_shot_chip_1",
            () -> new HeatPerShotChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 1.0F));
    public static final DeferredHolder<Item, Item> HEAT_PER_SHOT_CHIP_2 = ITEMS.register("heat_per_shot_chip_2",
            () -> new HeatPerShotChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 2.0F));
    public static final DeferredHolder<Item, Item> HEAT_PER_SHOT_CHIP_3 = ITEMS.register("heat_per_shot_chip_3",
            () -> new HeatPerShotChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 3.0F));
    public static final DeferredHolder<Item, Item> HEAT_PER_SHOT_CHIP_4 = ITEMS.register("heat_per_shot_chip_4",
            () -> new HeatPerShotChipItem(new Item.Properties(), GunAddonItem.AddonType.CHIP, 4.0F));

    public static final DeferredHolder<Item, Item> SCOPE = ITEMS.register("scope",
            () -> new ScopeAddonItem(new Item.Properties(), GunAddonItem.AddonType.SCOPE, 1.0F));

    public static final DeferredHolder<Item, PaintBucketItem> NIMBUS_PAINT_BUCKET = ITEMS.register("nimbus_paint_bucket",
            () -> new PaintBucketItem(new Item.Properties(), "nimbus", 0xFF2b9fe8, 0xFF817a99, 0));
    public static final DeferredHolder<Item, PaintBucketItem> ALIEN_PAINT_BUCKET = ITEMS.register("alien_paint_bucket",
            () -> new PaintBucketItem(new Item.Properties(), "alien", 0xFFbe54e0, 0xFF00f23f, 1));
    public static final DeferredHolder<Item, PaintBucketItem> COTTON_CANDY_PAINT_BUCKET = ITEMS.register("cotton_candy_paint_bucket",
            () -> new PaintBucketItem(new Item.Properties(), "cotton_candy", 0xFF0088c1, 0xFFb139b6, 2));
    public static final DeferredHolder<Item, PaintBucketItem> EVA_PAINT_BUCKET = ITEMS.register("eva_paint_bucket",
            () -> new PaintBucketItem(new Item.Properties(), "eva", 0xFF00f23f, 0xFFbe54e0, 3));
    public static final DeferredHolder<Item, PaintBucketItem> BUMBLEBEE_PAINT_BUCKET = ITEMS.register("bumblebee_paint_bucket",
            () -> new PaintBucketItem(new Item.Properties(), "bumblebee", 0xFFfa7900, 0xFFffd833, 4));
    public static final DeferredHolder<Item, PaintBucketItem> CRIMSON_PAINT_BUCKET = ITEMS.register("crimson_paint_bucket",
            () -> new PaintBucketItem(new Item.Properties(), "crimson", 0xFF86121f, 0xFF400a16, 5));

    public static class ModItemTab {

        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MekanismWeaponry.MOD_ID);

        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MEKW_ITEM_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                .icon(() -> Items.ANCIENT_DEBRIS.asItem().getDefaultInstance())
                .title(Component.translatable("item_group." + MekanismWeaponry.MOD_ID))
                .displayItems((displayParameters, output) -> {
                    ItemRegistry.ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(itemRegistryObject.get()));
                    BlockRegistry.BLOCKS.getEntries().forEach(iBlockProvider -> output.accept(iBlockProvider.get().asItem()));
                })
                .build());
    }
}
