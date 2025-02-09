package com.github.x3r.mekanism_weaponry.common.entity;

import com.github.x3r.mekanism_weaponry.common.registry.DamageTypeRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.EntityRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class GunProjectileEntity extends Projectile {

    protected final double damage;
    protected final Function<Entity, DamageSource> damageSource;

    protected GunProjectileEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.damage = 0;
        this.damageSource = null;
    }

    public GunProjectileEntity(EntityType<? extends Projectile> pEntityType, Level pLevel, double damage, Function<Entity, DamageSource> damageSource) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        this.setNoGravity(true);
        this.damage = damage;
        this.damageSource = damageSource;

    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {

    }

    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide()) {
            if(this.tickCount > 20*15) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }
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
        }
    }
    private void handleEntityCollision(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        if(!entity.equals(getOwner())) {
            entity.hurt(damageSource.apply(entity), (float) this.damage);
            Vec3 pos = hitResult.getLocation();
            ((ServerLevel) level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()),
                    pos.x, pos.y, pos.z, 15, 0, 0, 0, 0);
        }
    }

    private void handleBlockCollision(BlockHitResult hitResult) {
        BlockState state = level().getBlockState(hitResult.getBlockPos());
        if(state.getCollisionShape(level(), hitResult.getBlockPos(), CollisionContext.empty()).isEmpty() ||
                state.getVisualShape(level(), hitResult.getBlockPos(), CollisionContext.empty()).isEmpty()) {
            return;
        }
        Vec3 pos = hitResult.getLocation();
        ((ServerLevel) level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, level().getBlockState(hitResult.getBlockPos())),
                pos.x, pos.y, pos.z, 15, 0, 0, 0, 1);
        this.remove(RemovalReason.DISCARDED);
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
                AABB box = livingEntity.getBoundingBox().inflate(0.1F);
                Optional<Vec3> hitPos = box.clip(startVec, endVec);
                hitPos.ifPresent(vec3 -> collisions.add(new EntityHitResult(livingEntity, vec3)));
            }
        });
        return collisions;
    }
}
