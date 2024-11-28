package com.github.x3r.mekanism_weaponry.common.entity;

import com.github.x3r.mekanism_weaponry.common.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class ElectricityEntity extends Projectile {

    public static final EntityDataAccessor<Vector3f> DIRECTION = SynchedEntityData.defineId(ElectricityEntity.class, EntityDataSerializers.VECTOR3);

    private int lifetime;

    public ElectricityEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public ElectricityEntity(Player player, Vector3f direction, int lifetime) {
        this(EntityRegistry.ELECTRICITY.get(), player.level());
        this.lifetime = lifetime;
        this.setPos(player.position());
        setOwner(player);
        entityData.set(DIRECTION, direction);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.tickCount > lifetime) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DIRECTION, new Vector3f());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }
}
