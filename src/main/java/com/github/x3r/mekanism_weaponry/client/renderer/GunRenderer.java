package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.PaintBucketItem;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer;

public abstract class GunRenderer<T extends GunItem & GeoAnimatable> extends DynamicGeoItemRenderer<T> {

    private static ResourceLocation skinTexture;

    protected GunRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    protected @Nullable ResourceLocation getTextureOverrideForBone(GeoBone bone, T animatable, float partialTick) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player != null && bone.getParent() != null && bone.getParent().getParent() != null && bone.getParent().getParent().getName().equals("arms")) {
            ResourceLocation skin = getSkinResourceLocation(player);
            return skin;
        }
        return super.getTextureOverrideForBone(bone, animatable, partialTick);
    }

    private static ResourceLocation getSkinResourceLocation(LocalPlayer player) {
        if(skinTexture == null) {
            skinTexture = player.getSkin().texture();
        }
        return skinTexture;
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

    protected int getTextureIndex() {
        if(animatable.hasAddon(currentItemStack, PaintBucketItem.class)) {
            return ((PaintBucketItem) animatable.getAddon(currentItemStack, 1).getItem()).getIndex();
        }
        return 0;
    }

    @Override
    protected IntIntPair computeTextureSize(ResourceLocation texture) {
        if(texture.equals(skinTexture)) {
            return IntIntPair.of(64, 64);
        }
        if(texture.getNamespace().equals("minecraft") && texture.getPath().substring(0, texture.getPath().indexOf('/')).equals("skins")) {
            return IntIntPair.of(64, 64);
        }
        return super.computeTextureSize(texture);
    }


}
