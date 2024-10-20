package com.github.x3r.mekanism_weaponry.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;

public class GunChipItem extends Item {

    private final ChipType type;

    public GunChipItem(ChipType type) {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
        this.type = type;
    }

    public ChipType getChipType() {
        return type;
    }

    public enum ChipType implements IExtensibleEnum {
        FIRE_RATE_CHIP("fire_rate_chip");

        private final String id;

        ChipType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
