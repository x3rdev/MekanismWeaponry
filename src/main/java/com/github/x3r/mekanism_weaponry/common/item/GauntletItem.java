package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.MekanismWeaponryConfig;
import com.github.x3r.mekanism_weaponry.client.renderer.GauntletRenderer;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GauntletItem extends Item implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int energyUsage;

    public GauntletItem(Properties properties) {
        super(properties.setNoRepair().stacksTo(1)
                .requiredFeatures()
                .rarity(Rarity.UNCOMMON)
        );
        energyUsage = MekanismWeaponryConfig.CONFIG.getGauntletEnergyUsage();
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if(slot.equals(EquipmentSlot.MAINHAND)) {
            if(hasSufficientEnergy(stack)) {
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", MekanismWeaponryConfig.CONFIG.getGauntletDamage(), AttributeModifier.Operation.ADDITION));
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.0, AttributeModifier.Operation.ADDITION));
                return builder.build();
            } else {
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.6, AttributeModifier.Operation.ADDITION));
                return builder.build();
            }
        }
        return ImmutableMultimap.of();
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ItemEnergyCapability(stack, new EnergyStorage(
                MekanismWeaponryConfig.CONFIG.getGauntletEnergyCapacity(),
                MekanismWeaponryConfig.CONFIG.getGauntletEnergyTransfer()
        ));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(hasSufficientEnergy(stack)) {
            if(!target.level().isClientSide()) {
                getEnergyStorage(stack).extractEnergy(energyUsage, false);
                target.knockback(0.55, -attacker.getLookAngle().x, -attacker.getLookAngle().z);
                target.addDeltaMovement(new Vec3(0, 0.35, 0));
                target.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 1, 1);
                ((ServerLevel) target.level()).sendParticles(ParticleTypes.SMOKE, target.getX(), target.getY(), target.getZ(), 1, 0, 0, 0, 0);
            }
        } else {
            target.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1, 1);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    public boolean hasSufficientEnergy(ItemStack stack) {
        return getEnergyStorage(stack).getEnergyStored() >= energyUsage;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(
                Component.translatable("mekanism_weaponry.tooltip.gun_energy").withStyle(Style.EMPTY.withColor(0x2fb2d6)).append(
                        Component.literal(String.format("%d/%d FE", getEnergyStorage(pStack).getEnergyStored(), getEnergyStorage(pStack).getMaxEnergyStored())).withStyle(Style.EMPTY.withColor(0xFFFFFF))
                )
        );
    }

    public IEnergyStorage getEnergyStorage(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(() -> new RuntimeException("Missing energy capability"));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GauntletRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new GauntletRenderer();
                }

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
