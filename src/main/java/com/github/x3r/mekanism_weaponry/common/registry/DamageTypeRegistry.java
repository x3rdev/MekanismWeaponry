package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypeRegistry {

    private final Registry<DamageType> damageTypes;
    private final DamageSource laser;

    public DamageTypeRegistry(RegistryAccess registryAccess) {
        this.damageTypes = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
        this.laser = source(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "laser")));
    }

    private DamageSource source(ResourceKey<DamageType> pDamageTypeKey) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(pDamageTypeKey));
    }

    public DamageSource laser() {
        return this.laser;
    }
}
