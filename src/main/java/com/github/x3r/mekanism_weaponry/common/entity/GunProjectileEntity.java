package com.github.x3r.mekanism_weaponry.common.entity;

import com.github.x3r.mekanism_weaponry.common.registry.DamageTypeRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GunProjectileEntity extends Projectile{

    protected double damage = 1.0F;

    public GunProjectileEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {

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
            if(this.tickCount > 20*10) {
                this.remove(Entity.RemovalReason.KILLED);
                return;
            }
        }
    }
    private void handleEntityCollision(EntityHitResult hitResult) {
        if(!hitResult.getEntity().equals(getOwner())) {
            hitResult.getEntity().hurt(new DamageTypeRegistry(level().registryAccess()).laser(), (float) this.damage);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    private void handleBlockCollision(BlockHitResult hitResult) {
        BlockState state = level().getBlockState(hitResult.getBlockPos());
        if(state.getCollisionShape(level(), hitResult.getBlockPos(), CollisionContext.empty()).isEmpty() ||
                state.getVisualShape(level(), hitResult.getBlockPos(), CollisionContext.empty()).isEmpty()) {
            return;
        }
        this.remove(Entity.RemovalReason.KILLED);
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
}
