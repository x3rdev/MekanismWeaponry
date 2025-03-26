package com.github.x3r.mekanism_weaponry.common.item.addon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeatPerShotChipItem extends GunAddonItem {

    public HeatPerShotChipItem(Properties properties, AddonType addonType, float mul) {
        super(properties, addonType, mul);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(
                Component.translatable("mekanism_weaponry.tooltip.heat_per_shot_chip")
        );
    }
}
