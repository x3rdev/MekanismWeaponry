package com.github.x3r.mekanism_weaponry.common.recipe;

import com.github.x3r.mekanism_weaponry.common.item.GunChipItem;
import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

/*
    not exactly a recipe class but yeah whatever deal with it
 */
public class AnvilRecipes {

    public static void anvilUpdate(AnvilUpdateEvent event) {
        if(event.getLeft().getItem() instanceof GunItem gunItem
                && event.getRight().getItem() instanceof GunChipItem gunChipItem
                && gunItem.canInstallChip(event.getLeft(), event.getRight())) {
            ItemStack newStack = event.getLeft().copy();
            gunItem.addChip(newStack, event.getRight());
            event.setCost(1);
            event.setOutput(newStack);
        }
    }
}
