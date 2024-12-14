package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.client.ClientSetup;
import com.github.x3r.mekanism_weaponry.client.renderer.RailgunRenderer;
import com.github.x3r.mekanism_weaponry.common.entity.RodEntity;
import com.github.x3r.mekanism_weaponry.common.packet.ActivateGunPayload;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.ItemRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import com.github.x3r.mekanism_weaponry.common.scheduler.Scheduler;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
import java.util.function.Predicate;

public class RailgunItem extends AmmoGunItem implements GeoItem {

    private static final RawAnimation DEFAULT_MODE = RawAnimation.begin().then("default_mode", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation SECOND_MODE = RawAnimation.begin().then("second_mode", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation SWITCH_TO_DEFAULT = RawAnimation.begin().then("switch_to_default", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation SWITCH_TO_SECOND = RawAnimation.begin().then("switch_to_second", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation SHOT_DEFAULT = RawAnimation.begin().then("shot_default", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SHOT_SECOND = RawAnimation.begin().then("shot_second", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation RELOAD = RawAnimation.begin().then("reload", Animation.LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RailgunItem(Properties pProperties) {
        super(pProperties.component(DataComponentRegistry.RAILGUN_SECONDARY_MODE, false),
                20, 1000, 35, 1);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public Predicate<ItemStack> isValidAmmo(ItemStack gunStack) {
        return stack -> stack.is(ItemRegistry.STEEL_ROD.get());
    }

    @Override
    public void serverShoot(ItemStack stack, ServerPlayer player) {
        Level level = player.level();
        Vec3 pos = player.getEyePosition()
                .add(player.getLookAngle().normalize().scale(0.1));
        if(isReady(stack, player, level)) {
            setLastShotTick(stack, level.getGameTime());
            PacketDistributor.sendToPlayer(player, new ActivateGunPayload());

            float dmg = isSecondMode(stack) ? 24F : 16F;
            RodEntity rod = new RodEntity(player, pos, dmg, isSecondMode(stack));
            rod.setDeltaMovement(player.getLookAngle().normalize().scale(isSecondMode(stack) ? 4 : 3));
            level.addFreshEntity(rod);
            level.playSound(null, pos.x, pos.y, pos.z, SoundRegistry.RAILGUN_SHOOT.get(), SoundSource.PLAYERS, 4F, 1.0F);

            getLoadedAmmo(stack).shrink(1);

            getEnergyStorage(stack).extractEnergy(isSecondMode(stack) ? energyUsage*2 : energyUsage, false);
        } else {
            if(!hasSufficientEnergy(stack)) {
                level.playSound(null, pos.x, pos.y, pos.z, SoundRegistry.PLASMA_RIFLE_OUT_OF_ENERGY.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            if(!hasAmmo(stack)) {
                tryStartReload(stack, player);
            }
            stack.set(DataComponentRegistry.IS_SHOOTING, false);
        }
    }

    @Override
    public void clientShoot(ItemStack stack, Player player) {
        if(isSecondMode(stack)) {
            triggerAnim(player, GeoItem.getId(stack), "controller", "shot_second");
        } else {
            triggerAnim(player, GeoItem.getId(stack), "controller", "shot_default");
        }
        ClientSetup.recoil += 10;
    }

    @Override
    public void serverReload(ItemStack stack, ServerPlayer player) {
        player.getCooldowns().addCooldown(stack.getItem(), reloadTime);
        player.serverLevel().playSound(null,
                player.getX(), player.getY(), player.getZ(),
                SoundRegistry.RAILGUN_RELOAD, SoundSource.PLAYERS, 1.0F, 1.0F);
        Scheduler.schedule(() -> {
            if(player.getInventory().contains(stack)) {
                loadAmmo(stack, player);
            }
        }, 35);
    }

    @Override
    public void clientReload(ItemStack stack, Player player) {
        triggerAnim(player, GeoItem.getId(stack), "controller", "reload");
    }

    @Override
    public boolean canInstallAddon(ItemStack gunStack, ItemStack addonStack) {
        return true;
    }

    public boolean isSecondMode(ItemStack stack) {
        return stack.get(DataComponentRegistry.RAILGUN_SECONDARY_MODE.get()).booleanValue();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if(player.isCrouching()) {
            boolean secondMode = stack.get(DataComponentRegistry.RAILGUN_SECONDARY_MODE.get()).booleanValue();
            if(!secondMode) {
                triggerAnim(player, GeoItem.getId(stack), "controller", "switch_to_second");
                stack.set(DataComponentRegistry.RAILGUN_SECONDARY_MODE.get(), true);
            } else {
                triggerAnim(player, GeoItem.getId(stack), "controller", "switch_to_default");
                stack.set(DataComponentRegistry.RAILGUN_SECONDARY_MODE.get(), false);
            }
            player.getCooldowns().addCooldown(stack.getItem(), 20);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private RailgunRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new RailgunRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 1, state -> {
            if(!state.getController().isPlayingTriggeredAnimation()) {
                if (!isSecondMode(state.getData(DataTickets.ITEMSTACK))) {
                    state.setAnimation(DEFAULT_MODE);
                } else {
                    state.setAnimation(SECOND_MODE);
                }
            }
            if(state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).firstPerson()) {
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        })
                .receiveTriggeredAnimations()
                .triggerableAnim("switch_to_default", SWITCH_TO_DEFAULT)
                .triggerableAnim("switch_to_second", SWITCH_TO_SECOND)
                .triggerableAnim("shot_default", SHOT_DEFAULT)
                .triggerableAnim("shot_second", SHOT_SECOND)
                .triggerableAnim("reload", RELOAD)
        );
    }

    @Override
    public boolean isPerspectiveAware() {
        return true;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
