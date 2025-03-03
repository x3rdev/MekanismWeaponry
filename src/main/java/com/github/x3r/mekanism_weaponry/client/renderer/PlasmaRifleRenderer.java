package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.PlasmaRifleItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class PlasmaRifleRenderer extends GunRenderer<PlasmaRifleItem> {

    public PlasmaRifleRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "plasma_rifle")));
    }

    @Override
    public ResourceLocation getTextureLocation(PlasmaRifleItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, String.format("textures/item/plasma_rifle/plasma_rifle_%d.png", getTextureIndex()));
    }
}
