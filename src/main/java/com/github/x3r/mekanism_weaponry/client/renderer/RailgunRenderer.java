package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.RailgunItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class RailgunRenderer extends GunRenderer<RailgunItem> {
    public RailgunRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "railgun")));
    }
}
