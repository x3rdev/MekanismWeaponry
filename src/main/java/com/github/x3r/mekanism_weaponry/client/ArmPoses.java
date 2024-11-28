package com.github.x3r.mekanism_weaponry.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public class ArmPoses {

    public static final EnumProxy<HumanoidModel.ArmPose> MINIGUN_POSE = new EnumProxy<>(
            HumanoidModel.ArmPose.class, true, (IArmPoseTransformer) ArmPoses::minigunTransform
    );

    private static void minigunTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {

    }

}
