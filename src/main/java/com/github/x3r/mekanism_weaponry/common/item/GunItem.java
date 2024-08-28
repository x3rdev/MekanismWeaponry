package com.github.x3r.mekanism_weaponry.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GunItem extends Item {

    public GunItem(Properties pProperties) {
        super(pProperties.stacksTo(1).setNoRepair());
    }
}
