package com.github.x3r.mekanism_weaponry.common.entity;

import com.github.x3r.mekanism_weaponry.common.registry.DamageTypeRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.EntityRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class PlasmaEntity extends GunProjectileEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PlasmaEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public PlasmaEntity(ServerPlayer player, Vec3 pos, double damage) {
        super(EntityRegistry.PLASMA.get(), player.level(), damage,
                (entity) -> new DamageTypeRegistry(player.level().registryAccess()).plasma(entity));
        this.setPos(pos);
        setOwner(player);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
