package com.github.x3r.mekanism_weaponry.common.item;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemDecorator;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public abstract class AmmoGunItem extends GunItem {

    protected final int maxAmmo;

    protected AmmoGunItem(Properties pProperties, int cooldown, int energyUsage, int reloadTime, int maxAmmo) {
        super(pProperties, cooldown, energyUsage, reloadTime);
        this.maxAmmo = maxAmmo;
    }

    @Override
    public boolean isReady(ItemStack stack, ServerPlayer player, Level level) {
        return hasAmmo(stack) && super.isReady(stack, player, level);
    }

    @Override
    public boolean canReload(ItemStack stack, ServerPlayer serverPlayer) {
        return super.canReload(stack, serverPlayer) &&
                !getFirstAmmoStack(stack, serverPlayer).isEmpty();
    }

    public ItemStack getLoadedAmmo(ItemStack stack) {
        if(stack.getOrCreateTag().contains(ItemTags.LOADED_AMMO)) {
            return ItemStack.of(stack.getOrCreateTag().getCompound(ItemTags.LOADED_AMMO));
        }
        stack.getOrCreateTag().put(ItemTags.LOADED_AMMO, ItemStack.EMPTY.serializeNBT());
        return getLoadedAmmo(stack);
    }

    public void setLoadedAmmo(ItemStack stack, ItemStack ammoStack) {
        stack.getOrCreateTag().put(ItemTags.LOADED_AMMO, ammoStack.serializeNBT());
    }

    public void loadAmmo(ItemStack gunStack, ServerPlayer player) {
        ItemStack ammoInGun = getLoadedAmmo(gunStack);
        ItemStack ammoStack = getFirstAmmoStack(gunStack, player);
        int i = ammoInGun.getCount() + ammoStack.getCount() - maxAmmo;

        setLoadedAmmo(gunStack, new ItemStack(ammoStack.getItem(),
                Math.min(ammoInGun.getCount() + ammoStack.getCount(), maxAmmo)));
        ammoStack.setCount(Math.max(0, i));

        if(ammoInGun.getCount() < maxAmmo && !getFirstAmmoStack(gunStack, player).isEmpty()) {
            loadAmmo(gunStack, player);
        }
    }

    public abstract Predicate<ItemStack> isValidAmmo(ItemStack gunStack);

    public boolean ammoFitsInGun(ItemStack gunStack, ItemStack ammoStack) {
        ItemStack stackInGun = getLoadedAmmo(gunStack);
        if(stackInGun.isEmpty()) return true;
        return stackInGun.getCount() < maxAmmo && ammoStack.is(stackInGun.getItem());
    }

    public boolean hasAmmo(ItemStack gunStack) {
        return !getLoadedAmmo(gunStack).isEmpty();
    }

    public ItemStack getFirstAmmoStack(ItemStack gunStack, ServerPlayer player) {
        ItemStack offHandStack = player.getOffhandItem();
        if(isValidAmmo(gunStack).test(offHandStack) && ammoFitsInGun(gunStack, offHandStack)) {
                return offHandStack;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if(isValidAmmo(gunStack).test(stack) && ammoFitsInGun(gunStack, stack)) {
                    return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static IItemDecorator decorator() {
        return (guiGraphics, font, stack, xOffset, yOffset) -> {
            AmmoGunItem item = (AmmoGunItem) stack.getItem();
            float f = Math.min(1F, (float) item.getLoadedAmmo(stack).getCount()/item.maxAmmo);
            guiGraphics.fill(RenderType.guiOverlay(), xOffset+2, yOffset+13, xOffset+2+13, yOffset+13+2, 0xFF000000);
            for (int i = 0; i < MAX_BAR_WIDTH * f; i++) {
                guiGraphics.fill(RenderType.guiOverlay(), xOffset+2+i, yOffset+13, xOffset+2+i+1, yOffset+13+1, 0xFFFFFFFF);
            }

            return true;
        };
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(
                Component.translatable("mekanism_weaponry.tooltip.gun_loaded_ammo").withStyle(Style.EMPTY.withColor(0xc27ba0)).append(
                        Component.literal(String.format("%d/%d Steel Rods", getLoadedAmmo(pStack).getCount(), maxAmmo)).withStyle(Style.EMPTY.withColor(0xFFFFFF))
                )
        );
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
