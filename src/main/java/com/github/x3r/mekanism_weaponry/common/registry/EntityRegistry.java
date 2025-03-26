package com.github.x3r.mekanism_weaponry.common.registry;


import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.entity.PlasmaEntity;
import com.github.x3r.mekanism_weaponry.common.entity.RodEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MekanismWeaponry.MOD_ID);

    public static final RegistryObject<EntityType<PlasmaEntity>> PLASMA = ENTITY_TYPES.register("plasma",
            () -> EntityType.Builder.<PlasmaEntity>of(PlasmaEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .noSave()
                    .canSpawnFarFromPlayer()
                    .setTrackingRange(16)
                    .clientTrackingRange(16)
                    .updateInterval(20)
                    .build(new ResourceLocation(MekanismWeaponry.MOD_ID, "plasma").toString()));

    public static final RegistryObject<EntityType<RodEntity>> ROD = ENTITY_TYPES.register("rod",
            () -> EntityType.Builder.<RodEntity>of(RodEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .noSave()
                    .canSpawnFarFromPlayer()
                    .setTrackingRange(16)
                    .clientTrackingRange(16)
                    .updateInterval(20)
                    .build(new ResourceLocation(MekanismWeaponry.MOD_ID, "rod").toString()));
}
