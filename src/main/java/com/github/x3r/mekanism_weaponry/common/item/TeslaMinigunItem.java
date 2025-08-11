package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.MekanismWeaponryConfig;
import com.github.x3r.mekanism_weaponry.client.renderer.TeslaMinigunRenderer;
import com.github.x3r.mekanism_weaponry.client.sound.TeslaMinigunSoundInstance;
import com.github.x3r.mekanism_weaponry.common.item.addon.EnergyUsageChipItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.FireRateChipItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.PaintBucketItem;
import com.github.x3r.mekanism_weaponry.common.packet.ActivateGunClientPacket;
import com.github.x3r.mekanism_weaponry.common.packet.MekanismWeaponryPacketHandler;
import com.github.x3r.mekanism_weaponry.common.registry.DamageTypeRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import com.github.x3r.mekanism_weaponry.common.scheduler.Scheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class TeslaMinigunItem extends HeatGunItem implements GeoItem {

    private static final RawAnimation IDLE = RawAnimation.begin().then("idle", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation RELOAD = RawAnimation.begin().then("overheat", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SHOOT = RawAnimation.begin().then("fire", Animation.LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TeslaMinigunItem(Properties pProperties) {
        super(pProperties,
                MekanismWeaponryConfig.CONFIG.getTeslaMinigunCooldown(),
                MekanismWeaponryConfig.CONFIG.getTeslaMinigunEnergyUsage(),
                MekanismWeaponryConfig.CONFIG.getTeslaMinigunReloadTime(),
                MekanismWeaponryConfig.CONFIG.getTeslaMinigunHeatPerShot());
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ItemEnergyCapability(stack, new EnergyStorage(
                MekanismWeaponryConfig.CONFIG.getTeslaMinigunEnergyCapacity(),
                MekanismWeaponryConfig.CONFIG.getTeslaMinigunEnergyTransfer()
        ));
    }

    @Override
    public void serverShoot(ItemStack stack, ServerPlayer player) {
        Level level = player.level();
        if(isReady(stack, player, level)) {
            setShooting(stack, true);
            setLastShotTick(stack, level.getGameTime());
            MekanismWeaponryPacketHandler.sendToClient(new ActivateGunClientPacket(), player);
            getEnergyStorage(stack).extractEnergy(getEnergyUsage(stack), false);
            setHeat(stack, getHeat(stack) + getHeatPerShot(stack));
            Set<Entity> entitiesToHurt = new HashSet<>();
            for (int i = 0; i < 3; i++) {
                Vec3 hurtVolumeCenter = player.position().add(player.getLookAngle().normalize().scale(2*i));
                AABB hurtBox = AABB.ofSize(hurtVolumeCenter, 2, 2, 2);
                entitiesToHurt.addAll(level.getEntities(player, hurtBox));
                ((ServerLevel) level).sendParticles(ParticleTypes.ANGRY_VILLAGER, hurtVolumeCenter.x, hurtVolumeCenter.y, hurtVolumeCenter.z, 1, 0, 0, 0, 0);
            }
            entitiesToHurt.forEach(entity -> {
                if(entity instanceof LivingEntity) {
                    entity.hurt(new DamageTypeRegistry(player.level().registryAccess()).electricity(player), (float) MekanismWeaponryConfig.CONFIG.getTeslaMinigunDamage());
                }
            });
        } else {
            if(!hasSufficientEnergy(stack)) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegistry.GUN_OUT_OF_ENERGY.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            if(isOverheated(stack)) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            setShooting(stack, false);
        }
    }

    @Override
    public void clientShoot(ItemStack stack, Player player) {
        TeslaMinigunSoundInstance.playSound(player);
    }

    @Override
    public void serverReload(ItemStack stack, ServerPlayer player) {
        player.getCooldowns().addCooldown(stack.getItem(), getReloadTime(stack));
        for (int i = 0; i < getHeat(stack); i++) {
            Scheduler.schedule(() -> {
                if(player.getInventory().contains(stack)) {
                    setHeat(stack, Math.max(0, getHeat(stack) - 2));
                }
            }, 40+i);
        }
        Scheduler.schedule(() -> {
            if(player.getInventory().contains(stack)) {
                player.serverLevel().playSound(null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }, 40);
    }

    @Override
    public void clientReload(ItemStack stack, Player player) {
        triggerAnim(player, GeoItem.getId(stack), "controller", "reload");
    }

    @Override
    public boolean canInstallAddon(ItemStack gunStack, ItemStack addonStack) {
        if(addonStack.getItem().getClass().equals(EnergyUsageChipItem.class)) {
            return true;
        }
        if(addonStack.getItem().getClass().equals(FireRateChipItem.class)) {
            return true;
        }
        if(addonStack.getItem().getClass().equals(PaintBucketItem.class)) {
            return true;
        }
        return super.canInstallAddon(gunStack, addonStack);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private static final HumanoidModel.ArmPose MINIGUN_POSE = HumanoidModel.ArmPose.create("minigun", true, (model, livingEntity, humanoidArm) -> {
                float torsoAngle = 0.6F;
                model.body.yRot = torsoAngle;
                model.rightArm.yRot = torsoAngle;
                model.rightArm.x = (model.rightArm.x)* Mth.cos(torsoAngle);
                model.rightArm.z = (model.rightArm.z+5F)*Mth.sin(torsoAngle)-1;
                model.leftArm.yRot = torsoAngle;
                model.leftArm.x = (model.leftArm.x)*Mth.cos(torsoAngle);
                model.leftArm.z = (model.leftArm.z-5F)*Mth.sin(torsoAngle);

                model.rightArm.xRot = -0.5F;
                model.rightArm.yRot -= 0.5F;

                model.leftArm.xRot = -1F;
                model.leftArm.zRot = 0.1F;
            });
            private TeslaMinigunRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new TeslaMinigunRenderer();

                return this.renderer;
            }

            @Override
            public HumanoidModel.@NotNull ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return MINIGUN_POSE;
            }

        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller", 1, state -> {
            if(!state.getController().isPlayingTriggeredAnimation()) {
                state.setAnimation(IDLE);
            }
            if(state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).firstPerson()) {
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        })
                .receiveTriggeredAnimations()
                .triggerableAnim("reload", RELOAD)
                .triggerableAnim("shoot", SHOOT));
    }

    @Override
    public boolean isPerspectiveAware() {
        return true;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
