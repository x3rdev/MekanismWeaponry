package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.common.entity.ElectricityEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class ElectricityRenderer extends EntityRenderer<ElectricityEntity> {

    public ElectricityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ElectricityEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        ElectricityNode parentNode = buildNodes(entity, entity.getPosition(partialTick).toVector3f());
        parentNode.children.forEach(electricityNode -> {
            drawElectricity(parentNode.pos, electricityNode.pos, poseStack, bufferSource);
        });
    }

    private void drawElectricity(Vector3f v0, Vector3f v1, PoseStack poseStack, MultiBufferSource bufferSource) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.LIGHTNING);
        poseStack.pushPose();
        consumer.addVertex(poseStack.last(), v0.x, v0.y, v0.z).setColor(0xbdeb34);
        poseStack.popPose();
    }



    private ElectricityNode buildNodes(ElectricityEntity entity, Vector3f pos) {
        ElectricityNode node = new ElectricityNode(pos);
        Vector3f dir = entity.getEntityData().get(ElectricityEntity.DIRECTION);
        node.children.add(new ElectricityNode(pos.add(dir)));
        return node;
    }

    @Override
    public ResourceLocation getTextureLocation(ElectricityEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    private static class ElectricityNode {

        public Set<ElectricityNode> children;
        public Vector3f pos;

        public ElectricityNode(Vector3f pos) {
            this.children = new HashSet<>();
            this.pos = pos;
        }

    }
}
