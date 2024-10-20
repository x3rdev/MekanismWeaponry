package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.client.ClientSetup;
import com.github.x3r.mekanism_weaponry.client.renderer.PlasmaRifleRenderer;
import com.github.x3r.mekanism_weaponry.common.entity.PlasmaEntity;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.IItemDecorator;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class PlasmaRifleItem extends GunItem implements GeoItem {

    private static final RawAnimation IDLE = RawAnimation.begin().then("idle", Animation.LoopType.LOOP);
    private static final RawAnimation RELOAD = RawAnimation.begin().then("reload", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SHOOT = RawAnimation.begin().then("shot", Animation.LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PlasmaRifleItem(Properties pProperties) {
        super(pProperties, 8, 250);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public boolean isOffCooldown(ItemStack stack, long tick) {
        return super.isOffCooldown(stack, tick);
    }

    @Override
    public int getCooldown(ItemStack stack) {
        return super.getCooldown(stack) - getChipCount(stack, GunChipItem.ChipType.FIRE_RATE_CHIP);
    }

    @Override
    public void serverShoot(ItemStack stack, GunItem item, ServerPlayer player) {
        Level level = player.level();
        setTickOfLastShot(stack, level.getGameTime());
        Vec3 pos = player.getEyePosition()
                .add(0, 0, -0.025)
                .add(player.getLookAngle().normalize().scale(1.5));
        if(hasSufficientEnergy(stack)) {
            PlasmaEntity plasma = new PlasmaEntity(player.level(), pos, 1.0F);
            plasma.setDeltaMovement(player.getLookAngle().normalize().scale(3));
            level.addFreshEntity(plasma);
            level.playSound(null, pos.x, pos.y, pos.z, SoundRegistry.PLASMA_RIFLE_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            getEnergyStorage(stack).extractEnergy(energyUsage, false);
        } else {
            level.playSound(null, pos.x, pos.y, pos.z, SoundRegistry.PLASMA_RIFLE_OUT_OF_ENERGY.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public void clientShoot(ItemStack stack, GunItem item, LocalPlayer player) {
        triggerAnim(player, GeoItem.getId(stack), "controller", "shoot");
        ClientSetup.recoil = 5;
    }

    @Override
    public void serverReload(ItemStack stack, GunItem item, ServerPlayer player) {
    }

    @Override
    public void clientReload(ItemStack stack, GunItem item, LocalPlayer player) {
        triggerAnim(player, GeoItem.getId(stack), "controller", "reload");
    }

    @Override
    public boolean canInstallChip(ItemStack gunStack, ItemStack chipStack) {
        GunChipItem.ChipType chipType = ((GunChipItem) chipStack.getItem()).getChipType();
        if(chipType.equals(GunChipItem.ChipType.FIRE_RATE_CHIP)) {
            return true;
        }
        return false;
    }

    private static final int[] COLORS = new int[]{
            0xFF49ef1f, 0xFF58dd1e, 0xFF66ca1d, 0xFF75b81c,
            0xFF83a61b, 0xFF92931a, 0xFFa08119, 0xFFaf6f18,
            0xFFbd5c17, 0xFFcb4a16, 0xFFda3815, 0xFFe92514,
            0xFFf71313
    };

    public static IItemDecorator decorator() {
        return new IItemDecorator() {
            @Override
            public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
                for (int i = 0; i < 13; i++) {
                    guiGraphics.fill(RenderType.guiOverlay(), xOffset+2+i, yOffset+13, xOffset+2+i+1, yOffset+13+1, COLORS[i]);
                }
                guiGraphics.fill(RenderType.guiOverlay(), xOffset+2, yOffset+14, xOffset+2+13, yOffset+14+1, 0xFF000000);
                return true;
            }
        };
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
