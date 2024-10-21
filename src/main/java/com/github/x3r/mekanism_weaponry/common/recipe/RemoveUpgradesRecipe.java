package com.github.x3r.mekanism_weaponry.common.recipe;


import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import mekanism.common.registries.MekanismItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

//Again, not a recipe, deal with it
public class RemoveUpgradesRecipe {

    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if(event.getSide().isServer() && player.isCrouching()) {
            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();
            if(mainHand.getItem() instanceof GunItem gunItem && offHand.is(MekanismItems.CONFIGURATOR)) {
                List<ItemStack> chips = gunItem.getChips(mainHand);
                Vec3 angle = player.getLookAngle();
                for (ItemStack chip : chips) {
                    ItemEntity itemEntity = new ItemEntity(
                            event.getLevel(),
                            player.getX(), player.getY(), player.getZ(),
                            chip,
                            angle.x(), angle.y(), angle.z()
                    );
                    event.getLevel().addFreshEntity(itemEntity);
                }
                gunItem.clearChips(mainHand);
            }
        }
    }
}
