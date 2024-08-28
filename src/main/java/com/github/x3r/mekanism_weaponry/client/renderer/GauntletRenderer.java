package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.GauntletItem;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer;
import software.bernie.geckolib.util.RenderUtil;

public class GauntletRenderer extends DynamicGeoItemRenderer<GauntletItem> {

    private static final ResourceLocation FLAME_1 = ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "textures/item/gauntlet_flame_1.png");
    private static final ResourceLocation FLAME_2 = ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "textures/item/gauntlet_flame_2.png");

    public GauntletRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "gauntlet")));
    }

    @Override
    protected @Nullable ResourceLocation getTextureOverrideForBone(GeoBone bone, GauntletItem animatable, float partialTick) {
        if(bone.getName().equals("1")) {
            AnimatableTexture.setAndUpdate(FLAME_1);
            return FLAME_1;
        }
        if(bone.getName().equals("2")) {
            AnimatableTexture.setAndUpdate(FLAME_2);
            return FLAME_2;
        }
        return super.getTextureOverrideForBone(bone, animatable, partialTick);
    }

    @Override
    protected IntIntPair computeTextureSize(ResourceLocation texture) {
        return TEXTURE_DIMENSIONS_CACHE.computeIfAbsent(getTextureLocation(this.animatable), RenderUtil::getTextureDimensions);
    }
}
