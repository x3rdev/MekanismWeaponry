package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.client.renderer.PlasmaRifleRenderer;
import com.github.x3r.mekanism_weaponry.common.entity.LaserEntity;
import com.github.x3r.mekanism_weaponry.common.entity.PlasmaEntity;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.keyframe.event.ParticleKeyframeEvent;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class PlasmaRifleItem extends GunItem implements GeoItem {

    private static final RawAnimation IDLE = RawAnimation.begin().then("idle", Animation.LoopType.LOOP);
    private static final RawAnimation RELOAD = RawAnimation.begin().then("reload", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SHOOT = RawAnimation.begin().then("shot", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation ZOOM_IN = RawAnimation.begin().then("zoom_in", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation ZOOM_OUT = RawAnimation.begin().then("zoom_out", Animation.LoopType.HOLD_ON_LAST_FRAME);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PlasmaRifleItem(Properties pProperties) {
        super(pProperties, 15);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pPlayer instanceof ServerPlayer player) {
            triggerAnim(pPlayer, GeoItem.getOrAssignId(pPlayer.getItemInHand(pUsedHand), player.serverLevel()));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void serverShoot(ItemStack stack, GunItem item, ServerPlayer player) {
        Level level = player.level();
        if(isGunReady(stack, level.getGameTime())) {
            setTickOfLastShot(stack, level.getGameTime());
            Vec3 pos = player.getEyePosition()
                    .add(0, 0, -0.025)
                    .add(player.getLookAngle().normalize().scale(1.5));
            PlasmaEntity plasma = new PlasmaEntity(player.level(), pos, 1.0F);
            plasma.setDeltaMovement(player.getLookAngle().normalize().scale(3));
            level.addFreshEntity(plasma);
            level.playSound(null, pos.x, pos.y, pos.z, SoundRegistry.LASER_RIFLE_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            triggerAnim(player, GeoItem.getOrAssignId(stack, player.serverLevel()), "controller", "shoot");
        }
    }

    @Override
    public void clientShoot(ItemStack stack, GunItem item, LocalPlayer player) {

    }

    @Override
    public void serverReload(ItemStack stack, GunItem item, ServerPlayer player) {
        triggerAnim(player, GeoItem.getOrAssignId(stack, player.serverLevel()), "controller", "reload");
    }

    @Override
    public void clientReload(ItemStack stack, GunItem item, LocalPlayer player) {

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
                return PlayState.STOP;
            }
            return state.setAndContinue(IDLE);
        })
                .triggerableAnim("reload", RELOAD)
                .triggerableAnim("shoot", SHOOT));
        controllers.add(new AnimationController<>(this, "ads_controller", 1, state -> {
            return PlayState.CONTINUE;
        })
                .triggerableAnim("zoom_in", ZOOM_IN)
                .triggerableAnim("zoom_out", ZOOM_OUT));
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
