package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.MekanismWeaponryConfig;
import com.github.x3r.mekanism_weaponry.client.renderer.GauntletRenderer;
import mekanism.common.tags.MekanismTags;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class GauntletItem extends Item implements GeoItem {

    public static final Set<ItemAbility> ALWAYS_SUPPORTED_ACTIONS = Set.of(ItemAbilities.AXE_DIG, ItemAbilities.HOE_DIG, ItemAbilities.SHOVEL_DIG, ItemAbilities.PICKAXE_DIG,
            ItemAbilities.SWORD_DIG, ItemAbilities.SWORD_SWEEP);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int energyUsage;

    public GauntletItem(Properties properties) {
        super(properties.setNoRepair().stacksTo(1)
                .component(DataComponents.TOOL, new Tool(List.of(
                        Tool.Rule.deniesDrops(MekanismTags.Blocks.INCORRECT_FOR_MEKA_TOOL),
                        new Tool.Rule(new AnyHolderSet<>(BuiltInRegistries.BLOCK.asLookup()), Optional.empty(), Optional.of(true))
                ), -0.5F, 0))
                .attributes(createAttributes())
                .rarity(Rarity.UNCOMMON)
        );
        energyUsage = MekanismWeaponryConfig.CONFIG.getGauntletEnergyUsage();
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 6, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(hasSufficientEnergy(stack)) {
            if(!target.level().isClientSide()) {
                getEnergyStorage(stack).extractEnergy(energyUsage, false);
                target.knockback(0.55, -attacker.getLookAngle().x, -attacker.getLookAngle().z);
                target.addDeltaMovement(new Vec3(0, 0.35, 0));
                target.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS);
                ((ServerLevel) target.level()).sendParticles(ParticleTypes.SMOKE, target.getX(), target.getY(), target.getZ(), 1, 0, 0, 0, 0);
            }
        } else {
            target.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.CRAFTER_FAIL, SoundSource.PLAYERS);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    public boolean hasSufficientEnergy(ItemStack stack) {
        return getEnergyStorage(stack).getEnergyStored() >= energyUsage;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(
                Component.translatable("mekanism_weaponry.tooltip.gun_energy").withColor(0x2fb2d6).append(
                        Component.literal(String.format("%d/%d FE", getEnergyStorage(stack).getEnergyStored(), getEnergyStorage(stack).getMaxEnergyStored())).withColor(0xFFFFFF)
                )
        );
    }

    public IEnergyStorage getEnergyStorage(ItemStack stack) {
        return stack.getCapability(Capabilities.EnergyStorage.ITEM);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GauntletRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new GauntletRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
