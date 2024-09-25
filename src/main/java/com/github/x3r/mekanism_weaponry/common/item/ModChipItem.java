package com.github.x3r.mekanism_weaponry.common.item;

import net.minecraft.world.item.Item;

public class ModChipItem extends Item {

    public ModChipItem(Properties pProperties) {
        super(pProperties);
    }

    public record DataComponentGunMod(String name) {

    }
}
