package com.github.x3r.mekanism_weaponry.common.registry;


import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.entity.PlasmaEntity;
import com.github.x3r.mekanism_weaponry.common.entity.RodEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MekanismWeaponry.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<PlasmaEntity>> PLASMA = ENTITY_TYPES.register("plasma",
            () -> EntityType.Builder.<PlasmaEntity>of(PlasmaEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .noSave()
                    .canSpawnFarFromPlayer()
                    .setTrackingRange(16)
                    .clientTrackingRange(16)
                    .updateInterval(3)
                    .build(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "plasma").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<RodEntity>> ROD = ENTITY_TYPES.register("rod",
            () -> EntityType.Builder.<RodEntity>of(RodEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .noSave()
                    .canSpawnFarFromPlayer()
                    .setTrackingRange(16)
                    .clientTrackingRange(16)
                    .updateInterval(3)
                    .build(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "rod").toString()));
}
