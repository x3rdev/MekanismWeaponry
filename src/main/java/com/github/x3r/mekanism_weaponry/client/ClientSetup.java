package com.github.x3r.mekanism_weaponry.client;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.client.renderer.PlasmaRenderer;
import com.github.x3r.mekanism_weaponry.client.renderer.RodRenderer;
import com.github.x3r.mekanism_weaponry.client.screen.WeaponWorkbenchScreen;
import com.github.x3r.mekanism_weaponry.common.item.AmmoGunItem;
import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.github.x3r.mekanism_weaponry.common.item.HeatGunItem;
import com.github.x3r.mekanism_weaponry.common.packet.DeactivateGunPayload;
import com.github.x3r.mekanism_weaponry.common.packet.ReloadGunPayload;
import com.github.x3r.mekanism_weaponry.common.registry.*;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

@EventBusSubscriber(modid = MekanismWeaponry.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @Nullable
    private static ShaderInstance rendertypeElectricityShader;

    public static final Lazy<KeyMapping> RELOAD_MAPPING = Lazy.of(() -> new KeyMapping("key.mekanism_weaponry.reload", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_R, "key.categories.mekanism_weaponry"));

    // Neo Bus event, registered in mod class
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        while (RELOAD_MAPPING.get().consumeClick()) {
            if(stack.getItem() instanceof GunItem) {
                PacketDistributor.sendToServer(new ReloadGunPayload());
            }
        }
        if(!mc.options.keyAttack.isDown()) {
            if(stack.getItem() instanceof GunItem item && item.isShooting(stack)) {
                PacketDistributor.sendToServer(new DeactivateGunPayload());
            }
        }


        //recoil
        recoilO = recoil;
        if(recoil > 0) {
            recoil = Mth.sqrt(recoil);
            if (recoil < 0.01) {
                recoil = 0;
            }
        }


    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.PLASMA.get(), PlasmaRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ROD.get(), RodRenderer::new);
    }


    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        },
                ItemRegistry.PLASMA_RIFLE.get(),
                ItemRegistry.RAILGUN.get()
        );
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return ArmPoses.MINIGUN_POSE.getValue();
            }
        },
                ItemRegistry.TESLA_MINIGUN.get());
    }

    // Neo Bus event, registered in mod class
    public static void pressKey(InputEvent.InteractionKeyMappingTriggered event) {
        ItemStack stack = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND);
        if(event.isAttack() && stack.getItem() instanceof GunItem) {
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(RELOAD_MAPPING.get());
    }
    public static float recoilO;
    public static float recoil;


    // Neo Bus event, registered in mod class
    public static void cameraSetupEvent(ViewportEvent.ComputeCameraAngles event) {
        if(recoil > 0) {
            event.setPitch((float) (event.getPitch() - Mth.lerp(event.getPartialTick(), recoilO, recoil)));
        }
    }

    @SubscribeEvent
    public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
        event.register(ItemRegistry.PLASMA_RIFLE.get(), HeatGunItem.decorator());
        event.register(ItemRegistry.RAILGUN.get(), AmmoGunItem.decorator());
        event.register(ItemRegistry.TESLA_MINIGUN.get(), HeatGunItem.decorator());
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypeRegistry.WEAPON_WORKBENCH.get(), WeaponWorkbenchScreen::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.ROD_TRAIL.get(), ParticleRegistry.RodTrailParticleProvider::new);
    }

    @SubscribeEvent
    public static void registerRenderTypes(RegisterShadersEvent event) {
        ResourceProvider provider = event.getResourceProvider();
        try {
            event.registerShader(new ShaderInstance(provider, ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "electricity"), DefaultVertexFormat.NEW_ENTITY), shaderInstance -> {
                rendertypeElectricityShader = shaderInstance;
            });
        } catch (IOException e) {
            MekanismWeaponry.LOGGER.warn("Failed to load shader", e);
        }
    }

    public static ShaderInstance getElectricityShader() {
        return Objects.requireNonNull(rendertypeElectricityShader, "Attempted to get shader before they have finished loading.");
    }
}
