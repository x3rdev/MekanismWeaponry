package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.PaintBucketItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.util.RenderUtils;

public abstract class GunRenderer<T extends GunItem & GeoAnimatable> extends DynamicGeoItemRenderer<T> {

    protected GunRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    protected @Nullable ResourceLocation getTextureOverrideForBone(GeoBone bone, T animatable, float partialTick) {
        if(bone.getParent() != null && bone.getParent().getParent() != null && bone.getParent().getParent().getName().equals("arms")) {
            return Minecraft.getInstance().player.getSkinTextureLocation();
        }
        return super.getTextureOverrideForBone(bone, animatable, partialTick);
    }

    @Override
    protected boolean boneRenderOverride(PoseStack poseStack, GeoBone bone, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if(bone.getName().equals("rightSlim") || bone.getName().equals("leftSlim")) {
            return !Minecraft.getInstance().player.getModelName().equals("slim");
        }
        if(bone.getName().equals("rightWide") || bone.getName().equals("leftWide")) {
            return !Minecraft.getInstance().player.getModelName().equals("default");
        }
        return super.boneRenderOverride(poseStack, bone, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if(!renderPerspective.firstPerson() && bone.getName().equals("arms")) {
            return; // hides arm bones (and all children) when not in first person
        }
        if(renderPerspective.firstPerson() && animatable.isScoping(getCurrentItemStack())) {
            return; // hides gun if scoping
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected IntIntPair computeTextureSize(ResourceLocation texture) {
        return TEXTURE_DIMENSIONS_CACHE.computeIfAbsent(getTextureLocation(this.animatable), RenderUtils::getTextureDimensions);
    }

    protected int getTextureIndex() {
        if(animatable.hasAddon(currentItemStack, PaintBucketItem.class)) {
            return ((PaintBucketItem) animatable.getAddon(currentItemStack, 1).getItem()).getIndex();
        }
        return 0;
    }
}
