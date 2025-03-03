package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class DamageTypeRegistry {

    private final Registry<DamageType> damageTypes;

    private final ResourceKey<DamageType> plasma = ResourceKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "plasma"));
    private final ResourceKey<DamageType> rod = ResourceKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "rod"));
    private final ResourceKey<DamageType> electricity = ResourceKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "electricity"));

    public DamageTypeRegistry(RegistryAccess registryAccess) {
        this.damageTypes = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
    }

    public DamageSource plasma(Entity entity) {
        return source(plasma, entity);
    }

    public DamageSource rod(Entity entity) {
        return source(rod, entity);
    }

    public DamageSource electricity(Entity entity) {
        return source(electricity, entity);
    }

    private DamageSource source(ResourceKey<DamageType> key) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(key));
    }

    private DamageSource source(ResourceKey<DamageType> key, Entity entity) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(key), entity, entity, null);
    }
}
