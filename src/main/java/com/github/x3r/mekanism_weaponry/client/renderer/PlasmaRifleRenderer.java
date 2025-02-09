package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.PlasmaRifleItem;
import com.github.x3r.mekanism_weaponry.common.item.RailgunItem;
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
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

public class PlasmaRifleRenderer extends GunRenderer<PlasmaRifleItem> {

    public PlasmaRifleRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "plasma_rifle")));
    }

    @Override
    public ResourceLocation getTextureLocation(PlasmaRifleItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, String.format("textures/item/plasma_rifle/plasma_rifle_%d.png", getTextureIndex()));
    }
}
