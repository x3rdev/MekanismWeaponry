package com.github.x3r.mekanism_weaponry.common.registry;


import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.entity.LaserEntity;
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
                    .sized(0.125F, 0.125F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "laser").toString()));
}
