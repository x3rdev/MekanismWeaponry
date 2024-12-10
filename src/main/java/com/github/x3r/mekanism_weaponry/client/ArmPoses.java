package com.github.x3r.mekanism_weaponry.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public class ArmPoses {

    public static final EnumProxy<HumanoidModel.ArmPose> MINIGUN_POSE = new EnumProxy<>(
            HumanoidModel.ArmPose.class, true, (IArmPoseTransformer) ArmPoses::minigunTransform
    );

    private static void minigunTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        float torsoAngle = 0.6F;
        model.body.yRot = torsoAngle;
        model.rightArm.yRot = torsoAngle;
        model.rightArm.x = (model.rightArm.x)*Mth.cos(torsoAngle);
        model.rightArm.z = (model.rightArm.z+5F)*Mth.sin(torsoAngle)-1;
        model.leftArm.yRot = torsoAngle;
        model.leftArm.x = (model.leftArm.x)*Mth.cos(torsoAngle);
        model.leftArm.z = (model.leftArm.z-5F)*Mth.sin(torsoAngle);

        model.rightArm.xRot = -0.5F;
        model.rightArm.yRot -= 0.5F;

        model.leftArm.xRot = -1F;
        model.leftArm.zRot = 0.1F;
    }

}
