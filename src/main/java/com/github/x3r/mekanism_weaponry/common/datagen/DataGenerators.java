package com.github.x3r.mekanism_weaponry.common.datagen;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = MekanismWeaponry.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        BlockTagsProvider blockTagsProvider = new MWBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new MWItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter()));
        generator.addProvider(event.includeClient(), new MWModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new MWLangProvider(packOutput, "en_us"));
        generator.addProvider(event.includeClient(), new MWSoundDefProvider(packOutput, existingFileHelper));
    }
}
