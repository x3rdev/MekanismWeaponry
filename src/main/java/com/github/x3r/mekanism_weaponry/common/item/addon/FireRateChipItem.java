package com.github.x3r.mekanism_weaponry.common.item.addon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class FireRateChipItem extends GunAddonItem {

    public FireRateChipItem(Properties properties, AddonType addonType, float mul) {
        super(properties, addonType, mul);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(
                Component.translatable("mekanism_weaponry.tooltip.fire_rate_chip", (int) mul())
        );
    }
}
