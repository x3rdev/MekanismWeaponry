package com.github.x3r.mekanism_weaponry.client;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.client.renderer.PlasmaRenderer;
import com.github.x3r.mekanism_weaponry.client.renderer.RodRenderer;
import com.github.x3r.mekanism_weaponry.client.screen.WeaponWorkbenchScreen;
import com.github.x3r.mekanism_weaponry.common.item.AmmoGunItem;
import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.github.x3r.mekanism_weaponry.common.item.HeatGunItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.PaintBucketItem;
import com.github.x3r.mekanism_weaponry.common.packet.DeactivateGunPacket;
import com.github.x3r.mekanism_weaponry.common.packet.MekanismWeaponryPacketHandler;
import com.github.x3r.mekanism_weaponry.common.packet.ReloadGunServerPacket;
import com.github.x3r.mekanism_weaponry.common.registry.EntityRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.ItemRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.MenuTypeRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.ParticleRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import cpw.mods.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = MekanismWeaponry.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @Nullable
    private static ShaderInstance rendertypeElectricityShader;

    public static final KeyMapping RELOAD_MAPPING = new KeyMapping("key.mekanism_weaponry.reload", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_R, "key.categories.mekanism_weaponry");

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        forgeEventBus.addListener(ClientSetup::pressKey);
        forgeEventBus.addListener(ClientSetup::onClientTick);
        forgeEventBus.addListener(ClientSetup::cameraSetupEvent);
        forgeEventBus.addListener(ClientSetup::renderGui);
        forgeEventBus.addListener(ClientSetup::computeFov);

        event.enqueueWork(() -> {
            MenuScreens.register(MenuTypeRegistry.WEAPON_WORKBENCH.get(), WeaponWorkbenchScreen::new);
        });
    }

    // Neo Bus event, registered in mod class
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        while (RELOAD_MAPPING.consumeClick()) {
            if(stack.getItem() instanceof GunItem) {
                MekanismWeaponryPacketHandler.sendToServer(new ReloadGunServerPacket());
            }
        }
        if(!mc.options.keyAttack.isDown()) {
            if(stack.getItem() instanceof GunItem item && item.isShooting(stack)) {
                MekanismWeaponryPacketHandler.sendToServer(new DeactivateGunPacket());
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
        event.register(RELOAD_MAPPING);
    }

    private static float recoilO;
    private static float recoil;

    public static void addRecoil(int i) {
        recoil += i;
    }

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
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.ROD_TRAIL.get(), ParticleRegistry.RodTrailParticleProvider::new);
    }

    @SubscribeEvent
    public static void registerRenderTypes(RegisterShadersEvent event) {
        ResourceProvider provider = event.getResourceProvider();
        try {
            event.registerShader(new ShaderInstance(provider, new ResourceLocation(MekanismWeaponry.MOD_ID, "electricity"), DefaultVertexFormat.NEW_ENTITY), shaderInstance -> {
                rendertypeElectricityShader = shaderInstance;
            });
        } catch (IOException e) {
            MekanismWeaponry.LOGGER.warn("Failed to load shader", e);
        }
    }

    public static ShaderInstance getElectricityShader() {
        return Objects.requireNonNull(rendertypeElectricityShader, "Attempted to get shader before they have finished loading.");
    }

    private static float scopeScale;

    public static void renderGui(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.getCameraType().isFirstPerson()) {
            Player player = minecraft.player;
            if(player != null) {
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                scopeScale = Mth.lerp(0.5F * Minecraft.getInstance().getDeltaFrameTime(), scopeScale, 1F);
                if(stack.getItem() instanceof GunItem gunItem && gunItem.isScoping(stack)) {
                    renderScopeOverlay(event.getGuiGraphics(), getScopeLocation(stack), scopeScale);
                } else {
                    scopeScale = 0.5F;
                    PostChain postEffect = Minecraft.getInstance().gameRenderer.postEffect;
                    if(postEffect != null && postEffect.getName().equals("mekanism_weaponry:shaders/post/scope_blur.json")) {
                        Minecraft.getInstance().gameRenderer.shutdownEffect();
                    }
                }
            }
        }
    }

    private static ResourceLocation getScopeLocation(ItemStack stack) {
        GunItem gun = ((GunItem) stack.getItem());
        ItemStack paintItem = gun.getAddon(stack, 1);
        int index = 0;
        if(paintItem.isEmpty()) {
            index = 3;
        }
        if(paintItem.is(ItemRegistry.ALIEN_PAINT_BUCKET.get())) {
            index = 2;
        }
        if(paintItem.is(ItemRegistry.COTTON_CANDY_PAINT_BUCKET.get())) {
            index = 0;
        }
        if(paintItem.is(ItemRegistry.EVA_PAINT_BUCKET.get())) {
            index = 0;
        }
        if(paintItem.is(ItemRegistry.BUMBLEBEE_PAINT_BUCKET.get())) {
            index = 0;
        }
        if(paintItem.is(ItemRegistry.CRIMSON_PAINT_BUCKET.get())) {
            index = 1;
        }
        return new ResourceLocation(MekanismWeaponry.MOD_ID, String.format("textures/misc/scope_overlay_%d.png", index));
    }

    private static void renderScopeOverlay(GuiGraphics guiGraphics, ResourceLocation scopeResource, float scopeScale) {
        float f = Math.min(guiGraphics.guiWidth(), guiGraphics.guiHeight());
        float f1 = Math.min(guiGraphics.guiWidth() / f, guiGraphics.guiHeight() / f) * scopeScale;
        int i = Mth.floor(f * f1);
        int j = Mth.floor(f * f1);
        int k = (guiGraphics.guiWidth() - i) / 2;
        int l = (guiGraphics.guiHeight() - j) / 2;
        RenderSystem.enableBlend();
        guiGraphics.blit(scopeResource, k, l, -90, 0.0F, 0.0F, i, j, i, j);
        RenderSystem.disableBlend();
        Minecraft.getInstance().gameRenderer.loadEffect(new ResourceLocation(MekanismWeaponry.MOD_ID, ("shaders/post/scope_blur.json")));
    }

    public static void computeFov(ComputeFovModifierEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.getCameraType().isFirstPerson()) {
            Player player = minecraft.player;
            if(player != null) {
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if(stack.getItem() instanceof GunItem gunItem && gunItem.isScoping(stack)) {
                    event.setNewFovModifier(0.2F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerPaintColors(RegisterColorHandlersEvent.Item event) {
        ItemRegistry.ITEMS.getEntries().forEach(itemDeferredHolder -> {
            if(itemDeferredHolder.get() instanceof PaintBucketItem item) {
                event.register((stack, tintIndex) -> item.getColor(tintIndex), item);
            }
        });
    }
}
