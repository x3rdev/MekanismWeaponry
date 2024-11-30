package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.client.ClientSetup;
import com.github.x3r.mekanism_weaponry.client.shader.MWRenderTypes;
import com.github.x3r.mekanism_weaponry.common.entity.ElectricityEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.HashSet;
import java.util.Set;

public class ElectricityRenderer extends EntityRenderer<ElectricityEntity> {

    public static ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "textures/entity/electricity.png");

    public ElectricityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ElectricityEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        drawElectricity(entity.getNodes(), poseStack, bufferSource, packedLight);
    }

    private void drawElectricity(ElectricityEntity.ElectricityNode node, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(node != null) {
            for (ElectricityEntity.ElectricityNode electricityNode : node.children) {
                drawElectricity(node.pos, electricityNode.pos, poseStack, bufferSource, packedLight);
                drawElectricity(electricityNode, poseStack, bufferSource, packedLight);
            }
        }
    }

    private void drawElectricity(Vec3 v0, Vec3 v1, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        VertexConsumer consumer = bufferSource.getBuffer(MWRenderTypes.electricity(LOCATION));
        poseStack.pushPose();
        Vec3 corner0 = new Vec3(v0.x, v0.y, v0.z);
        Vec3 corner1 = new Vec3(v1.x, v1.y, v1.z);

        longFace(poseStack, consumer, corner0, corner1, new Vector3i(0, 1, 0), packedLight);
        longFace(poseStack, consumer, corner0, corner1, new Vector3i(0, 0, 1), packedLight);
        longFace(poseStack, consumer, corner0, corner1, new Vector3i(0, -1, 0), packedLight);
        longFace(poseStack, consumer, corner0, corner1, new Vector3i(0, 0, -1), packedLight);

        poseStack.popPose();
    }

    private void longFace(PoseStack poseStack, VertexConsumer consumer, Vec3 corner0, Vec3 corner1, Vector3i cross, int packedLight) {
         // controls width of strip
        Vec3 perp = corner0.vectorTo(corner1).cross(new Vec3(cross.x, cross.y, cross.z)).normalize().scale(0.01);
        vertex(poseStack, consumer, corner0.subtract(perp), cross.x, cross.y, cross.z, packedLight);
        vertex(poseStack, consumer, corner0.add(perp), cross.x, cross.y, cross.z, packedLight);
        vertex(poseStack, consumer, corner1.add(perp), cross.x, cross.y, cross.z, packedLight);
        vertex(poseStack, consumer, corner1.subtract(perp), cross.x, cross.y, cross.z, packedLight);
    }

    private void vertex(PoseStack poseStack, VertexConsumer consumer, Vec3 vec, int normalX, int normalY, int normalZ, int packedLight) {
        consumer.addVertex(poseStack.last(), vec.toVector3f())
                .setColor(255, 255, 255, 255)
                .setUv1(0, 0)
                .setUv(16, 16)
                .setLight(packedLight)
                .setNormal(poseStack.last(), normalX, normalY, normalZ);
    }

    @Override
    public ResourceLocation getTextureLocation(ElectricityEntity entity) {
        return LOCATION;
    }
}
