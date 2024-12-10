package com.github.x3r.mekanism_weaponry.common.entity;

import com.github.x3r.mekanism_weaponry.common.particle.RodParticle;
import com.github.x3r.mekanism_weaponry.common.registry.DamageTypeRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.EntityRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.ParticleRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RodEntity extends GunProjectileEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean strongAttack = false;

    public RodEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RodEntity(ServerPlayer player, Vec3 pos, double damage, boolean strongAttack) {
        super(EntityRegistry.ROD.get(), player.level(), damage,
                (entity) -> new DamageTypeRegistry(player.level().registryAccess()).rod(entity));
        this.setPos(pos);
        this.strongAttack = strongAttack;
        setOwner(player);
    }

    @Override
    public void tick() {
        super.tick();
        if(strongAttack && !level().isClientSide()) {
            ((ServerLevel) level()).sendParticles((ParticleOptions) ParticleRegistry.ROD_TRAIL.get(), getX(), getY(), getZ(), 1, 0, 0, 0, 0);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
