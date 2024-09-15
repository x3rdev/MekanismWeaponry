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

public class LaserEntity extends Projectile {

    public static final EntityDataAccessor<Integer> LASER_COLOR = SynchedEntityData.defineId(LaserEntity.class, EntityDataSerializers.INT);

    private double damage = 1.0F;

    public LaserEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
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

    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide()) {
            handleCollisions();
        }
        this.setPos(this.position().add(this.getDeltaMovement()));
    }

    private void handleCollisions() {
        Vec3 startVec = this.position();
        Vec3 endVec = this.position().add(this.getDeltaMovement());
        List<BlockHitResult> blockCollisions = traceBlockCollisions(startVec, endVec);
        List<EntityHitResult> entityCollisions = traceEntityCollisions(startVec, endVec, this.level().getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1)));

        List<HitResult> collisions = new ArrayList<>();
        collisions.addAll(blockCollisions);
        collisions.addAll(entityCollisions);
        collisions.sort(Comparator.comparingDouble(o -> o.getLocation().distanceToSqr(startVec)));
        for(HitResult hitResult : collisions) {
            if(hitResult instanceof EntityHitResult entityHitResult) {
                handleEntityCollision(entityHitResult);
            }
            if(hitResult instanceof BlockHitResult blockHitResult) {
                handleBlockCollision(blockHitResult);
            }
            if(this.tickCount < 20*10) {
                this.remove(RemovalReason.KILLED);
                return;
            }
        }
    }
    private void handleEntityCollision(EntityHitResult hitResult) {
        hitResult.getEntity().hurt(new DamageTypeRegistry(level().registryAccess()).laser(), (float) this.damage);
        this.remove(RemovalReason.KILLED);
    }

    private void handleBlockCollision(BlockHitResult hitResult) {
        BlockState state = level().getBlockState(hitResult.getBlockPos());
        if(!state.getVisualShape(level(), hitResult.getBlockPos(), CollisionContext.empty()).isEmpty()) {
            this.remove(RemovalReason.KILLED);
        }
    }

    public List<BlockHitResult> traceBlockCollisions(Vec3 startVec, Vec3 endVec) {
        List<BlockHitResult> collisions = new ArrayList<>();
        double scale = this.getBoundingBox().getSize()/endVec.subtract(startVec).length();
        Vec3 increment = endVec.subtract(startVec).scale(scale);
        for(int i = 0; i < 1/scale; i++) {
            Vec3 vPos = startVec.add(increment.scale(i));
            BlockPos pos = new BlockPos(Mth.floor(vPos.x), Mth.floor(vPos.y), Mth.floor(vPos.z));
            BlockState state = level().getBlockState(pos);
            if(!state.isAir()) {
                collisions.add(new BlockHitResult(vPos, Direction.getNearest(vPos.x, vPos.y, vPos.z).getOpposite(), pos, true));
            }
        }
        return collisions;
    }

    public List<EntityHitResult> traceEntityCollisions(Vec3 startVec, Vec3 endVec, List<Entity> candidates) {
        List<EntityHitResult> collisions = new ArrayList<>();
        candidates.forEach(entity -> {
            if(entity instanceof LivingEntity livingEntity) {
                AABB box = livingEntity.getBoundingBox();
                Optional<Vec3> hitPos = box.clip(startVec, endVec);
                hitPos.ifPresent(vec3 -> collisions.add(new EntityHitResult(livingEntity, vec3)));
            }
        });
        return collisions;
    }

    public int getColor() {
        return this.getEntityData().get(LASER_COLOR);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(LASER_COLOR, 0xFFFFFFFF);
    }

}
