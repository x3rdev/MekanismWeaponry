package com.github.x3r.mekanism_weaponry.common.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RailgunItem extends GunItem implements GeoItem {

    private static final RawAnimation DEFAULT_MODE = RawAnimation.begin().then("default_mode", Animation.LoopType.HOLD_ON_LAST_FRAME);


    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RailgunItem(Properties pProperties) {
        super(pProperties, 10, 20, 1000);
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
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
