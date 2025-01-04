package com.github.x3r.mekanism_weaponry.common.item.addon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class PaintBucketItem extends GunAddonItem {

    private final Component colorschemeName;

    public PaintBucketItem(Properties properties, Component colorschemeName) {
        super(properties, AddonType.PAINT, 0.0F);
        this.colorschemeName = colorschemeName;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(
                Component.translatable("mekanism_weaponry.tooltip.addon_effect_multiplier").append(": ").withColor(0x2fb2d6).append(
                        colorschemeName
                ).withColor(0xFFFFFF)
        );
    }
}
