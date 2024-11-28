package com.github.x3r.mekanism_weaponry.common.registry;


import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.entity.ElectricityEntity;
import com.github.x3r.mekanism_weaponry.common.entity.LaserEntity;
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

    public static final DeferredHolder<EntityType<?>, EntityType<LaserEntity>> LASER = ENTITY_TYPES.register("laser",
            () -> EntityType.Builder.<LaserEntity>of(LaserEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "laser").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<PlasmaEntity>> PLASMA = ENTITY_TYPES.register("plasma",
            () -> EntityType.Builder.<PlasmaEntity>of(PlasmaEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "plasma").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<RodEntity>> ROD = ENTITY_TYPES.register("rod",
            () -> EntityType.Builder.<RodEntity>of(RodEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "rod").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<ElectricityEntity>> ELECTRICITY = ENTITY_TYPES.register("electricity",
            () -> EntityType.Builder.<ElectricityEntity>of(ElectricityEntity::new, MobCategory.MISC)
                    .sized(5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .noSave()
                    .noSummon()
                    .build(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "electricity").toString()));

}
