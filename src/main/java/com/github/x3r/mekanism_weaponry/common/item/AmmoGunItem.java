package com.github.x3r.mekanism_weaponry.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class AmmoGunItem extends GunItem {

    public AmmoGunItem(Properties pProperties, int cooldown, int energyUsage) {
        super(pProperties, cooldown, energyUsage);
    }

    public abstract boolean isValidAmmo(ItemStack gunStack, ItemStack ammoStack);

    public ItemStack getFirstAmmoStack(ItemStack gunStack, Player player) {
        ItemStack offHandStack = player.getOffhandItem();
        if(isValidAmmo(gunStack, offHandStack)) {
            return offHandStack;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if(isValidAmmo(gunStack, stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
