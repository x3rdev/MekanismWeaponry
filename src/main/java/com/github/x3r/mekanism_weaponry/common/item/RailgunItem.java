package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.MekanismWeaponryConfig;
import com.github.x3r.mekanism_weaponry.client.ClientSetup;
import com.github.x3r.mekanism_weaponry.client.renderer.RailgunRenderer;
import com.github.x3r.mekanism_weaponry.common.datagen.MWItemTagsProvider;
import com.github.x3r.mekanism_weaponry.common.entity.RodEntity;
import com.github.x3r.mekanism_weaponry.common.item.addon.EnergyUsageChipItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.FireRateChipItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.PaintBucketItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.ScopeAddonItem;
import com.github.x3r.mekanism_weaponry.common.packet.ActivateGunPayload;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import com.github.x3r.mekanism_weaponry.common.scheduler.Scheduler;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
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
                MekanismWeaponryConfig.CONFIG.getRailgunCooldown(),
                MekanismWeaponryConfig.CONFIG.getRailgunEnergyUsage(),
                MekanismWeaponryConfig.CONFIG.getRailgunReloadTime(),
                MekanismWeaponryConfig.CONFIG.getRailgunMaxAmmo());
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public Predicate<ItemStack> isValidAmmo(ItemStack gunStack) {
        return stack -> stack.is(MWItemTagsProvider.STEEL_ROD);
    }

    @Override
    public void serverShoot(ItemStack stack, ServerPlayer player) {
        Level level = player.level();
        Vec3 lookAngle = player.getLookAngle();
        Vec3 pos = player.getEyePosition()
                .add(lookAngle.normalize().scale(0.1));
        if(isReady(stack, player, level)) {
            setLastShotTick(stack, level.getGameTime());
            PacketDistributor.sendToPlayer(player, new ActivateGunPayload());

            double dmg = MekanismWeaponryConfig.CONFIG.getRailgunDamage() * getScale(stack);
            RodEntity rod = new RodEntity(player, pos, dmg, isSecondMode(stack));
            rod.setDeltaMovement(lookAngle.add(0, 0.015, 0).normalize().scale(3 * getScale(stack)));
            level.addFreshEntity(rod);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegistry.RAILGUN_SHOOT.get(), SoundSource.PLAYERS, 3F, 1.0F);

            setLoadedAmmo(stack, getLoadedAmmo(stack).copyWithCount(getLoadedAmmo(stack).getCount()-1));

            getEnergyStorage(stack).extractEnergy((int) (getEnergyUsage(stack)*getScale(stack)), false);

            if(isSecondMode(stack)) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 4));
            }

        } else {
            if(!hasSufficientEnergy(stack)) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegistry.GUN_OUT_OF_ENERGY.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            if(!hasAmmo(stack)) {
                tryStartReload(stack, player);
            }
            stack.set(DataComponentRegistry.IS_SHOOTING, false);
        }
    }

    private double getScale(ItemStack stack) {
        return isSecondMode(stack) ? MekanismWeaponryConfig.CONFIG.getRailgunSecondModeScale() : 1F;
    }

    @Override
    public void clientShoot(ItemStack stack, Player player) {
        if(isSecondMode(stack)) {
            triggerAnim(player, GeoItem.getId(stack), "controller", "shot_second");
            player.push(player.getLookAngle().scale(-0.75F));
        } else {
            triggerAnim(player, GeoItem.getId(stack), "controller", "shot_default");
        }
        ClientSetup.addRecoil(10);
    }

    @Override
    public void serverReload(ItemStack stack, ServerPlayer player) {
        player.getCooldowns().addCooldown(stack.getItem(), getReloadTime(stack));
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
        if(addonStack.getItem().getClass().equals(EnergyUsageChipItem.class)) {
            return true;
        }
        if(addonStack.getItem().getClass().equals(FireRateChipItem.class)) {
            return true;
        }
        if(addonStack.getItem().getClass().equals(PaintBucketItem.class)) {
            return true;
        }
        return addonStack.getItem().getClass().equals(ScopeAddonItem.class);
    }

    public boolean isSecondMode(ItemStack stack) {
        return stack.get(DataComponentRegistry.RAILGUN_SECONDARY_MODE.get());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if(player.isCrouching()) {
            boolean secondMode = stack.get(DataComponentRegistry.RAILGUN_SECONDARY_MODE.get());
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(
                Component.translatable("mekanism_weaponry.tooltip.railgun_mode_label").withColor(0x76ff26).append(
                        Component.translatable(isSecondMode(stack) ?
                                        "mekanism_weaponry.tooltip.railgun_second_mode" :
                                        "mekanism_weaponry.tooltip.railgun_first_mode")
                                .withStyle(Style.EMPTY.withColor(0xFFFFFF)).append(
                                        Component.literal(" [SHIFT + RMB]").withColor(0x5c5c5c)
                                ))
        );
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
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
