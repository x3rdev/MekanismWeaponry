package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.RailgunItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.ScopeAddonItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class RailgunRenderer extends GunRenderer<RailgunItem> {

    public RailgunRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "railgun")){
            @Override
            public ResourceLocation getTextureResource(RailgunItem animatable) {
                return ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "textures/item/railgun_paint_0.png");
            }
        });
    }

    @Override
    protected boolean boneRenderOverride(PoseStack poseStack, GeoBone bone, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        if(bone.getName().equals("scope")) {
            return !hasScopeAddon();
        }
        return super.boneRenderOverride(poseStack, bone, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
    }

    private boolean hasScopeAddon() {
        return animatable.hasAddon(currentItemStack, ScopeAddonItem.class);
    }
}
