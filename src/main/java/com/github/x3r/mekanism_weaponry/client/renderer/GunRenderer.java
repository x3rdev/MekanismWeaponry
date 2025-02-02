package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.github.x3r.mekanism_weaponry.common.item.PlasmaRifleItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.PaintBucketItem;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer;
import software.bernie.geckolib.util.RenderUtil;

public abstract class GunRenderer<T extends GunItem & GeoAnimatable> extends DynamicGeoItemRenderer<T> {

    protected GunRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    protected @Nullable ResourceLocation getTextureOverrideForBone(GeoBone bone, T animatable, float partialTick) {
        if(bone.getParent() != null && bone.getParent().getParent() != null && bone.getParent().getParent().getName().equals("arms")) {
            return Minecraft.getInstance().player.getSkin().texture();
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
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if(!renderPerspective.firstPerson() && bone.getName().equals("arms")) {
            return; // hides arm bones (and all children) when not in first person
        }
        if(renderPerspective.firstPerson() && getCurrentItemStack().get(DataComponentRegistry.IS_SCOPING.get())) {
            return; // hides gun if scoping
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    protected IntIntPair computeTextureSize(ResourceLocation texture) {
        return TEXTURE_DIMENSIONS_CACHE.computeIfAbsent(getTextureLocation(this.animatable), RenderUtil::getTextureDimensions);
    }

    protected int getTextureIndex() {
        if(animatable.hasAddon(currentItemStack, PaintBucketItem.class)) {
            return ((PaintBucketItem) animatable.getAddon(currentItemStack, 1).getItem()).getIndex();
        }
        return 0;
    }
}
