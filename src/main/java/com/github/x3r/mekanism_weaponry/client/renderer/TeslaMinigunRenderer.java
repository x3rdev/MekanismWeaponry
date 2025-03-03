package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.client.shader.MWRenderTypes;
import com.github.x3r.mekanism_weaponry.common.item.TeslaMinigunItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3i;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

import java.util.HashSet;
import java.util.Set;

public class TeslaMinigunRenderer extends GunRenderer<TeslaMinigunItem> {

    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "textures/entity/electricity.png");

    private final RandomSource source;

    private ElectricityNode node = null;
    private long lastNodeGenTick = 0;

    public TeslaMinigunRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "tesla_minigun")));
        source = Minecraft.getInstance().player.getRandom();
    }

    @Override
    public ResourceLocation getTextureLocation(TeslaMinigunItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, String.format("textures/item/tesla_minigun/tesla_minigun_%d.png", getTextureIndex()));

    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
        if(transformType != ItemDisplayContext.GUI && ((TeslaMinigunItem) stack.getItem()).isShooting(stack)) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.64, -0.25); // magic numbers for aligning lightning to barrel of gun
            poseStack.scale(5, 5, 5);
            if(transformType.firstPerson()) {
                poseStack.scale(3, 3, 2);
            }
            drawElectricity(buildNodes(4), poseStack, bufferSource, packedLight);
            poseStack.popPose();
        }
    }

    private ElectricityNode buildNodes(int maxDepth) {
        if(lastNodeGenTick == Minecraft.getInstance().level.getGameTime()) {
            return node;
        }
        node = buildNodes(new ElectricityNode(new Vec3(0, 0, 0)), 0, maxDepth);
        lastNodeGenTick = Minecraft.getInstance().level.getGameTime();
        return node;
    }

    private ElectricityNode buildNodes(ElectricityNode curr, int depth, int maxDepth) {
        if(depth < maxDepth) {
            Vector3f dir = new Vector3f(0, -0.01F,-4);
            for (int i = 0; i < source.nextInt(4); i++) {
                ElectricityNode newNode = new ElectricityNode(new Vec3(
                        (float) (curr.pos.x + (dir.x*1/(depth+1))+(source.nextGaussian()* Mth.sqrt(depth+0.1F)/3)),
                        (float) (curr.pos.y + (dir.y*1/(depth+1))+(source.nextGaussian()*Mth.sqrt(depth+0.1F)/4)),
                        (float) (curr.pos.z + (dir.z*1/(depth+1))+(source.nextGaussian()*Mth.sqrt(depth+0.1F)/3))
                ));
                buildNodes(newNode, depth+1, maxDepth);
                curr.children.add(newNode);
            }
        }
        return curr;
    }

    private void drawElectricity(ElectricityNode node, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(node != null) {
            for (ElectricityNode electricityNode : node.children) {
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

    public static class ElectricityNode {
        private final Set<ElectricityNode> children;
        private final Vec3 pos;

        public ElectricityNode(Vec3 pos) {
            this.children = new HashSet<>();
            this.pos = pos;
        }
    }
}
