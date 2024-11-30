package com.github.x3r.mekanism_weaponry.common.entity;

import com.github.x3r.mekanism_weaponry.client.renderer.ElectricityRenderer;
import com.github.x3r.mekanism_weaponry.common.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class ElectricityEntity extends Projectile {

    public static final EntityDataAccessor<Vector3f> DIRECTION = SynchedEntityData.defineId(ElectricityEntity.class, EntityDataSerializers.VECTOR3);

    private ElectricityNode nodes;

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
        if(!this.level().isClientSide() && this.tickCount > lifetime) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        if(this.level().isClientSide()) {
            nodes = buildNodes(5);
        }
    }

    public ElectricityNode getNodes() {
        return nodes;
    }

    private ElectricityNode buildNodes(int maxDepth) {
        return buildNodes(new ElectricityNode(new Vec3(0, 1, 0)), 0, maxDepth);
    }

    private ElectricityNode buildNodes(ElectricityNode curr, int depth, int maxDepth) {
        if(depth < maxDepth) {
            RandomSource source = getRandom();
            Vector3f dir = getEntityData().get(DIRECTION);
            for (int i = 0; i < source.nextInt(4); i++) {
                ElectricityNode node = new ElectricityNode(new Vec3(
                        (float) (curr.pos.x + (dir.x*1/(depth+1))+(source.nextGaussian()*Mth.sqrt(depth+0.1F)/4)),
                        (float) (curr.pos.y + (dir.y*1/(depth+1))+(source.nextGaussian()*Mth.sqrt(depth+0.1F)/5)),
                        (float) (curr.pos.z + (dir.z*1/(depth+1))+(source.nextGaussian()*Mth.sqrt(depth+0.1F)/4))
                ));
                buildNodes(node, depth+1, maxDepth);
                curr.children.add(node);
            }
        }
        return curr;
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

    public static class ElectricityNode {
        public Set<ElectricityNode> children;
        public Vec3 pos;

        public ElectricityNode(Vec3 pos) {
            this.children = new HashSet<>();
            this.pos = pos;
        }
    }
}
