package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.IItemDecorator;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class AmmoGunItem extends GunItem {

    protected final int maxAmmo;

    public AmmoGunItem(Properties pProperties, int cooldown, int energyUsage, int maxAmmo) {
        super(pProperties.component(DataComponentRegistry.LOADED_AMMO.get(),
                new DataComponentLoadedAmmo()), cooldown, energyUsage);
        this.maxAmmo = maxAmmo;
    }

    @Override
    public boolean isReady(ItemStack stack, Level level) {
        return hasAmmo(stack) && super.isReady(stack, level);
    }

    @Override
    public boolean canReload(ItemStack stack, ServerPlayer serverPlayer) {
        return !getFirstAmmoStack(stack, serverPlayer).isEmpty();
    }

    public ItemStack getLoadedAmmo(ItemStack gunStack) {
        return gunStack.get(DataComponentRegistry.LOADED_AMMO).getStack();
    }

    public void loadAmmo(ItemStack gunStack, ServerPlayer player) {
        ItemStack ammoInGun = gunStack.get(DataComponentRegistry.LOADED_AMMO).getStack();
        ItemStack ammoStack = getFirstAmmoStack(gunStack, player);
        int i = ammoInGun.getCount() + ammoStack.getCount() - maxAmmo;

        gunStack.get(DataComponentRegistry.LOADED_AMMO).setStack(new ItemStack(ammoStack.getItem(), Math.min(ammoInGun.getCount() + ammoStack.getCount(), maxAmmo)));
        ammoStack.setCount(Math.max(0, i));

        if(ammoInGun.getCount() < maxAmmo && !getFirstAmmoStack(gunStack, player).isEmpty()) {
            loadAmmo(gunStack, player);
        }
    }

    public abstract Predicate<ItemStack> isValidAmmo(ItemStack gunStack);

    public boolean ammoFitsInGun(ItemStack gunStack, ItemStack ammoStack) {
        ItemStack stackInGun = gunStack.get(DataComponentRegistry.LOADED_AMMO).getStack();
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

    public static final class DataComponentLoadedAmmo {

        private ItemStack stack;

        public DataComponentLoadedAmmo() {
            this(ItemStack.EMPTY);
        }

        public DataComponentLoadedAmmo(ItemStack stack) {
            this.stack = stack;
        }

        public ItemStack getStack() {
            return stack;
        }

        public void setStack(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (DataComponentLoadedAmmo) obj;
            return Objects.equals(this.stack, that.stack);
        }

        @Override
        public int hashCode() {
            return Objects.hash(stack);
        }

        @Override
        public String toString() {
            return "DataComponentLoadedAmmo[" +
                    "stack=" + stack + ']';
        }


    }
}
