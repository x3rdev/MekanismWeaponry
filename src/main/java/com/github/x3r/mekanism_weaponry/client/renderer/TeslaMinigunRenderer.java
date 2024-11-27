package com.github.x3r.mekanism_weaponry.client.renderer;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.item.PlasmaRifleItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class TeslaMinigunRenderer extends GunRenderer<PlasmaRifleItem> {

    public static EnumProxy<HumanoidModel.ArmPose> MINIGUN_POSE = new EnumProxy<>(
            HumanoidModel.ArmPose.class, true, new MinigunArmPoseTransformer()
    );

    public TeslaMinigunRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "tesla_minigun")));
    }

    private static class MinigunArmPoseTransformer implements IArmPoseTransformer {

        @Override
        public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
            
        }
    }
}
