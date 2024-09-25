package com.github.x3r.mekanism_weaponry.common.entity;

import com.github.x3r.mekanism_weaponry.common.registry.DamageTypeRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LaserEntity extends GunProjectileEntity {

    public static final EntityDataAccessor<Integer> LASER_COLOR = SynchedEntityData.defineId(LaserEntity.class, EntityDataSerializers.INT);

    public LaserEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public LaserEntity(Level pLevel, Vec3 pos, double damage, int color) {
        this(EntityRegistry.LASER.get(), pLevel);
        this.setPos(pos);
        this.damage = damage;
        this.getEntityData().set(LASER_COLOR, color);
    }

    public LaserEntity(Level pLevel, Vec3 pos, double damage, int r, int g, int b, int a) {
        this(pLevel, pos, damage, (r << 24) + (g << 16) + (b << 8) + a);
    }


    public int getColor() {
        return this.getEntityData().get(LASER_COLOR);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(LASER_COLOR, 0xFFFFFFFF);
    }

}
