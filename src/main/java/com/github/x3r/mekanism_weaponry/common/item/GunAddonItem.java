package com.github.x3r.mekanism_weaponry.common.item;

import net.minecraft.world.item.Item;

public class GunAddonItem extends Item {

    private final AddonType addonType;

    public GunAddonItem(Properties properties, AddonType addonType) {
        super(properties.stacksTo(1));
        this.addonType = addonType;
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
