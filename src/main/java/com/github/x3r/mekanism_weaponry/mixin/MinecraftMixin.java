package com.github.x3r.mekanism_weaponry.mixin;

import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.github.x3r.mekanism_weaponry.common.packet.ActivateGunClientPacket;
import com.github.x3r.mekanism_weaponry.common.packet.ActivateGunServerPacket;
import com.github.x3r.mekanism_weaponry.common.packet.MekanismWeaponryPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow @Nullable public LocalPlayer player;

    @Shadow @Final public Options options;

    @Shadow @Final public MouseHandler mouseHandler;

    @Shadow @Nullable public ClientLevel level;

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;continueAttack(Z)V"), cancellable = true)
    private void handleKeybinds(CallbackInfo ci) {
        if(player == null) return;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(stack.getItem() instanceof GunItem && this.mouseHandler.isMouseGrabbed()) {
            if(this.options.keyAttack.isDown()) {
                leftClickGun(stack);
                ci.cancel();
            }
        }

    }

    private void leftClickGun(ItemStack stack) {
        if(stack.getItem() instanceof GunItem item && item.isOffCooldown(stack, level.getGameTime())) {
            MekanismWeaponryPacketHandler.sendToServer(new ActivateGunServerPacket());
            item.setLastShotTick(stack, level.getGameTime());
        }
    }
}
