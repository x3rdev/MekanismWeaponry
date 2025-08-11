package com.github.x3r.mekanism_weaponry.common.datagen;

import com.github.x3r.mekanism_weaponry.common.registry.BlockRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MWLootTableProvider extends LootTableProvider {

    public MWLootTableProvider(PackOutput pOutput) {
        super(pOutput, Set.of(), List.of(
                new SubProviderEntry(SoulForgeBlockSubProvider::new, LootContextParamSets.BLOCK)
        ));
    }

    private static class SoulForgeBlockSubProvider extends BlockLootSubProvider {

        protected SoulForgeBlockSubProvider() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return BlockRegistry.BLOCKS.getEntries().stream()
                    .map(RegistryObject::get).toList();
        }

        @Override
        protected void generate() {
            dropSelf(BlockRegistry.WEAPON_WORKBENCH.get());
        }
    }
}
