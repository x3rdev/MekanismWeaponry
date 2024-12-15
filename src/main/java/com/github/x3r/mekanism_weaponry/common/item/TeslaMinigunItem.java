package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.client.renderer.TeslaMinigunRenderer;
import com.github.x3r.mekanism_weaponry.client.sound.TeslaMinigunSoundInstance;
import com.github.x3r.mekanism_weaponry.common.packet.ActivateGunPayload;
import com.github.x3r.mekanism_weaponry.common.registry.DamageTypeRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import com.github.x3r.mekanism_weaponry.common.scheduler.Scheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class TeslaMinigunItem extends HeatGunItem implements GeoItem {

    private static final RawAnimation IDLE = RawAnimation.begin().then("idle", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation RELOAD = RawAnimation.begin().then("overheat", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SHOOT = RawAnimation.begin().then("fire", Animation.LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TeslaMinigunItem(Properties pProperties) {
        super(pProperties, 4, 100, 100, 5);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void serverShoot(ItemStack stack, ServerPlayer player) {
        Level level = player.level();
        Vec3 pos = player.getEyePosition()
                .add(player.getLookAngle().normalize().scale(0.1));
        if(isReady(stack, player, level)) {
            stack.set(DataComponentRegistry.IS_SHOOTING, true);
            setLastShotTick(stack, level.getGameTime());
            PacketDistributor.sendToPlayer(player, new ActivateGunPayload());
            getEnergyStorage(stack).extractEnergy(getEnergyUsage(stack), false);
            setHeat(stack, getHeat(stack) + getHeatPerShot(stack));

            AABB hurtBox = new AABB(
                    player.position().add(player.getLookAngle().normalize().scale(5)).subtract(0.5, 0.5, 0.5),
                    player.position().add(player.getLookAngle().normalize().scale(5)).add(0.5, 1.5, 0.5)
                    ).inflate(4);
            level.getEntities(player, hurtBox).forEach(entity -> {
                entity.hurt(new DamageTypeRegistry(player.level().registryAccess()).electricity(player), 10);
            });

        } else {
            if(!hasSufficientEnergy(stack)) {
                level.playSound(null, pos.x, pos.y, pos.z, SoundRegistry.PLASMA_RIFLE_OUT_OF_ENERGY.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            if(isOverheated(stack)) {
                level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            stack.set(DataComponentRegistry.IS_SHOOTING, false);
        }
    }

    @Override
    public void clientShoot(ItemStack stack, Player player) {
        SoundManager manager = Minecraft.getInstance().getSoundManager();
        SoundInstance instance = new TeslaMinigunSoundInstance(player);
        if(!manager.isActive(instance)) {
            manager.play(instance);
        }
    }

    @Override
    public void serverReload(ItemStack stack, ServerPlayer player) {
        player.getCooldowns().addCooldown(stack.getItem(), getReloadTime(stack));
        for (int i = 0; i < getHeat(stack); i++) {
            Scheduler.schedule(() -> {
                if(player.getInventory().contains(stack)) {
                    setHeat(stack, getHeat(stack) - 2);
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
        return false;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private TeslaMinigunRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new TeslaMinigunRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 1, state -> {
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
