package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.index.qual.Positive;

public abstract class GunItem extends Item {

    protected GunItem(Properties pProperties, @Positive int cooldown) {
        super(pProperties.stacksTo(1).setNoRepair()
                .component(DataComponentRegistry.COOLDOWN.get(), new DataComponentCooldown(cooldown, 0))
                .component(DataComponentRegistry.ZOOM.get(), new DataComponentZoom(false)));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return false;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {

        return true; //Cancel swing
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.getItem().equals(newStack.getItem());
    }

    @Positive // Should not be zero, 1 will shoot every tick
    public int getGunCooldown(ItemStack stack) {
        return stack.get(DataComponentRegistry.COOLDOWN.get()).cooldown();
    }

    public long getTickOfLastShot(ItemStack stack) {
        return stack.get(DataComponentRegistry.COOLDOWN.get()).tickOfLastShot();
    }

    public void setTickOfLastShot(ItemStack stack, long tick) {
        stack.set(DataComponentRegistry.COOLDOWN.get(), new DataComponentCooldown(getGunCooldown(stack), tick));
    }

    public void toggleZoom(ItemStack stack) {
        stack.set(DataComponentRegistry.ZOOM.get(), new DataComponentZoom(!stack.get(DataComponentRegistry.ZOOM.get()).zoomed()));
    }

    public boolean isGunReady(ItemStack stack, long tick) {
        return tick - getTickOfLastShot(stack) >= getGunCooldown(stack);
    }

    public abstract void serverShoot(ItemStack stack, GunItem item, ServerPlayer player);

    public abstract void clientShoot(ItemStack stack, GunItem item, LocalPlayer player);

    public abstract void serverReload(ItemStack stack, GunItem item, ServerPlayer player);

    public abstract void clientReload(ItemStack stack, GunItem item, LocalPlayer player);

    public record DataComponentCooldown(int cooldown, long tickOfLastShot) {}
    public record DataComponentHeat(){}
    public record DataComponentZoom(boolean zoomed){}
}
