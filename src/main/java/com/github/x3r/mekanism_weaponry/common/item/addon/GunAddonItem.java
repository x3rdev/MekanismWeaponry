package com.github.x3r.mekanism_weaponry.common.item.addon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.text.DecimalFormat;
import java.util.List;

public abstract class GunAddonItem extends Item {

    public static DecimalFormat DF = new DecimalFormat("0.#");

    private final AddonType addonType;
    private final float mul;

    protected GunAddonItem(Properties properties, AddonType addonType, float mul) {
        super(properties.stacksTo(1));
        this.addonType = addonType;
        this.mul = mul;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(
                Component.translatable("mekanism_weaponry.tooltip.addon_effect_multiplier").append(": ").withColor(0x2fb2d6).append(
                        Component.literal(DF.format(mul)+"x").withColor(0xFFFFFF)
                )
        );
    }

    public float mul() {
        return mul;
    }

    public AddonType getAddonType() {
        return addonType;
    }

    public enum AddonType {
        CHIP,
        PAINT,
        SCOPE
    }
}
