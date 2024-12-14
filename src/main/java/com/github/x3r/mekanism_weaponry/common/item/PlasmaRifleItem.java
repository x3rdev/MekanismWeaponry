package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.client.ClientSetup;
import com.github.x3r.mekanism_weaponry.client.renderer.PlasmaRifleRenderer;
import com.github.x3r.mekanism_weaponry.common.entity.PlasmaEntity;
import com.github.x3r.mekanism_weaponry.common.packet.ActivateGunPayload;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import com.github.x3r.mekanism_weaponry.common.scheduler.Scheduler;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

public class PlasmaRifleItem extends HeatGunItem implements GeoItem {

    private static final RawAnimation IDLE = RawAnimation.begin().then("idle", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation RELOAD = RawAnimation.begin().then("reload", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SHOOT = RawAnimation.begin().then("shot", Animation.LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PlasmaRifleItem(Properties pProperties) {
        super(pProperties, 6, 250, 45, 20);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void serverShoot(ItemStack stack, ServerPlayer player) {
        Level level = player.level();
        Vec3 pos = player.getEyePosition()
                .add(player.getLookAngle().normalize().scale(0.1));
        if(isReady(stack, player, level)) {
            setLastShotTick(stack, level.getGameTime());
            PacketDistributor.sendToPlayer(player, new ActivateGunPayload());

            PlasmaEntity plasma = new PlasmaEntity(player, pos, 8.0F);
            plasma.setDeltaMovement(player.getLookAngle().normalize().scale(3));
            level.addFreshEntity(plasma);
            level.playSound(null, pos.x, pos.y, pos.z, SoundRegistry.PLASMA_RIFLE_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            getEnergyStorage(stack).extractEnergy(energyUsage, false);
            setHeat(stack, getHeat(stack) + heatPerShot);
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
        triggerAnim(player, GeoItem.getId(stack), "controller", "shoot");
        ClientSetup.recoil += 5;
    }

    @Override
    public void serverReload(ItemStack stack, ServerPlayer player) {
        player.getCooldowns().addCooldown(stack.getItem(), reloadTime);
        Scheduler.schedule(() -> {
            if(player.getInventory().contains(stack)) {
                setHeat(stack, 0);
                player.serverLevel().playSound(null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }, 45);
    }

    @Override
    public void clientReload(ItemStack stack, Player player) {
        triggerAnim(player, GeoItem.getId(stack), "controller", "reload");
    }

    @Override
    public boolean canInstallAddon(ItemStack gunStack, ItemStack addonStack) {
        return true;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private PlasmaRifleRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new PlasmaRifleRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 1, state -> {
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
