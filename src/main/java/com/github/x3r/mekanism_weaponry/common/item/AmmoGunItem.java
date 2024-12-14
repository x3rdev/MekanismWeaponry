package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.IItemDecorator;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class AmmoGunItem extends GunItem {

    protected final int maxAmmo;

    protected AmmoGunItem(Properties pProperties, int cooldown, int energyUsage, int reloadTime, int maxAmmo) {
        super(pProperties.component(DataComponentRegistry.LOADED_AMMO.get(),
                new DataComponentLoadedAmmo()), cooldown, energyUsage, reloadTime);
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

    public ItemStack getLoadedAmmo(ItemStack gunStack) {
        return gunStack.get(DataComponentRegistry.LOADED_AMMO).stack();
    }

    public void setLoadedAmmo(ItemStack gunStack, ItemStack ammoStack) {
        gunStack.set(DataComponentRegistry.LOADED_AMMO, new DataComponentLoadedAmmo(ammoStack));
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
            if(isValidAmmo(gunStack).test(stack) && ammoFitsInGun(gunStack, offHandStack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static IItemDecorator decorator() {
        return new IItemDecorator() {
            @Override
            public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
                AmmoGunItem item = (AmmoGunItem) stack.getItem();
                float f = Math.min(1F, (float) item.getLoadedAmmo(stack).getCount()/item.maxAmmo);
                guiGraphics.fill(RenderType.guiOverlay(), xOffset+2, yOffset+13, xOffset+2+13, yOffset+13+2, 0xFF000000);
                for (int i = 0; i < MAX_BAR_WIDTH * f; i++) {
                    guiGraphics.fill(RenderType.guiOverlay(), xOffset+2+i, yOffset+13, xOffset+2+i+1, yOffset+13+1, 0xFFFFFFFF);
                }

                return true;
            }
        };
    }

    public record DataComponentLoadedAmmo(ItemStack stack) {
        public DataComponentLoadedAmmo() {
            this(ItemStack.EMPTY);
        }
    }
}
