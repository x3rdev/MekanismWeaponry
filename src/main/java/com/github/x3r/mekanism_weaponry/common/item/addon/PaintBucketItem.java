package com.github.x3r.mekanism_weaponry.common.item.addon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class PaintBucketItem extends GunAddonItem {

    private final String colorschemeName;
    private final int color0;
    private final int color1;
    private final int index;

    public PaintBucketItem(Properties properties, String colorschemeName, int color0, int color1, int index) {
        super(properties, AddonType.PAINT, 0.0F);
        this.colorschemeName = colorschemeName;
        this.color0 = color0;
        this.color1 = color1;
        this.index = index;
    }

    public int getColor(int tintIndex) {
        switch (tintIndex) {
            case 1 -> {
                return color0;
            }
            case 2 -> {
                return color1;
            }
            default -> {
                return -1;
            }
        }
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(
                Component.translatable("mekanism_weaponry.tooltip.color_desc."+colorschemeName).withColor(0x2fb2d6)
        );
    }
}
