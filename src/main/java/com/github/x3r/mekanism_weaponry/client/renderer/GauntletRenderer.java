package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.GauntletItem;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer;

public class GauntletRenderer extends DynamicGeoItemRenderer<GauntletItem> {

    private static final ResourceLocation FLAME_1 = ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "textures/item/gauntlet_flame_1.png");
    private static final ResourceLocation FLAME_2 = ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "textures/item/gauntlet_flame_2.png");

    public GauntletRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "gauntlet")));
    }

    @Override
    protected @Nullable ResourceLocation getTextureOverrideForBone(GeoBone bone, GauntletItem animatable, float partialTick) {
        if(bone.getName().equals("1")) {
            return FLAME_1;
        }
        if(bone.getName().equals("2")) {
            return FLAME_2;
        }
        return super.getTextureOverrideForBone(bone, animatable, partialTick);
    }
}
