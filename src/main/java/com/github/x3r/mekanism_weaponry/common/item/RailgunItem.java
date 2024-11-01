package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.client.renderer.PlasmaRifleRenderer;
import com.github.x3r.mekanism_weaponry.client.renderer.RailgunRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class RailgunItem extends GunItem implements GeoItem {

    private static final RawAnimation DEFAULT_MODE = RawAnimation.begin().then("default_mode", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation SECOND_MODE = RawAnimation.begin().then("second_mode", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation SWITCH_TO_DEFAULT = RawAnimation.begin().then("switch_to_default", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation SWITCH_TO_SECOND = RawAnimation.begin().then("switch_to_second", Animation.LoopType.HOLD_ON_LAST_FRAME);
    private static final RawAnimation SHOT_DEFAULT = RawAnimation.begin().then("shot_default", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SHOT_SECOND = RawAnimation.begin().then("shot_second", Animation.LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RailgunItem(Properties pProperties) {
        super(pProperties, 20, 1000);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void serverShoot(ItemStack stack, GunItem item, ServerPlayer player) {

    }

    @Override
    public void clientShoot(ItemStack stack, GunItem item, Player player) {

    }

    @Override
    public void serverReload(ItemStack stack, GunItem item, ServerPlayer player) {

    }

    @Override
    public void clientReload(ItemStack stack, GunItem item, Player player) {

    }

    @Override
    public boolean canInstallAddon(ItemStack gunStack, ItemStack addonStack) {
        return true;
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
            if(state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).firstPerson()) {
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        })
                .receiveTriggeredAnimations()
                .triggerableAnim("switch_to_default", SWITCH_TO_DEFAULT)
                .triggerableAnim("switch_to_second", SWITCH_TO_SECOND)
                .triggerableAnim("shot_default", SHOT_DEFAULT)
                .triggerableAnim("shot_second", SHOT_SECOND));
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
