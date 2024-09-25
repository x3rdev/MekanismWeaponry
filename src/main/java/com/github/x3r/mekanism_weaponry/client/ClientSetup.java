package com.github.x3r.mekanism_weaponry.client;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.client.renderer.LaserRenderer;
import com.github.x3r.mekanism_weaponry.client.renderer.PlasmaRenderer;
import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.github.x3r.mekanism_weaponry.common.packet.ActivateGunPayload;
import com.github.x3r.mekanism_weaponry.common.packet.ReloadGunPayload;
import com.github.x3r.mekanism_weaponry.common.registry.EntityRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.ItemRegistry;
import com.github.x3r.mekanism_weaponry.mixin.MinecraftMixin;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.InputType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.network.PacketDistributor;
import org.checkerframework.checker.units.qual.K;

@EventBusSubscriber(modid = MekanismWeaponry.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static final Lazy<KeyMapping> RELOAD_MAPPING = Lazy.of(() -> new KeyMapping("key.mekanism_weaponry.reload", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_R, "key.categories.mekanism_weaponry"));

    // Neo Bus event, registered in mod class
    public static void onClientTick(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        while (RELOAD_MAPPING.get().consumeClick()) {
            if(stack.getItem() instanceof GunItem item) {
                PacketDistributor.sendToServer(new ReloadGunPayload());
                item.clientReload(stack, item, player);
            }
        }
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.LASER.get(), LaserRenderer::new);
        event.registerEntityRenderer(EntityRegistry.PLASMA.get(), PlasmaRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return HumanoidModel.ArmPose.BOW_AND_ARROW;
            }


        }, ItemRegistry.PLASMA_RIFLE.get()); // All guns?
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
}
