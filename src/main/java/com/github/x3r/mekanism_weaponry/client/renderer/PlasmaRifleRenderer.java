package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.PlasmaRifleItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

public class PlasmaRifleRenderer extends DynamicGeoItemRenderer<PlasmaRifleItem> {

    private static final ResourceLocation MUZZLE_FLASH_LOCATION = ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "textures/item/plasma_rifle_muzzle_flash.png");

    public PlasmaRifleRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "plasma_rifle")));
    }

    @Override
    protected @Nullable ResourceLocation getTextureOverrideForBone(GeoBone bone, PlasmaRifleItem animatable, float partialTick) {
        if(bone.getParent() != null && bone.getParent().getParent() != null && bone.getParent().getParent().getName().equals("arms")) {
            return Minecraft.getInstance().player.getSkin().texture();
        }
        if(bone.getName().equals("muzzle_flash")) {
            return MUZZLE_FLASH_LOCATION;
        }
        return super.getTextureOverrideForBone(bone, animatable, partialTick);
    }

    @Override
    protected boolean boneRenderOverride(PoseStack poseStack, GeoBone bone, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        if(bone.getName().equals("rightSlim") || bone.getName().equals("leftSlim")) {
            return !Minecraft.getInstance().player.getSkin().model().equals(PlayerSkin.Model.SLIM);
        }
        if(bone.getName().equals("rightWide") || bone.getName().equals("leftWide")) {
            return !Minecraft.getInstance().player.getSkin().model().equals(PlayerSkin.Model.WIDE);
        }
        return super.boneRenderOverride(poseStack, bone, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, PlasmaRifleItem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if(!renderPerspective.firstPerson() && bone.getName().equals("arms")) {
            return; // hides arm bones (and all children) when not in first person
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    protected IntIntPair computeTextureSize(ResourceLocation texture) {
        return TEXTURE_DIMENSIONS_CACHE.computeIfAbsent(getTextureLocation(this.animatable), RenderUtil::getTextureDimensions);
    }
}
