package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.common.item.addon.HeatPerShotChipItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemDecorator;

import java.util.List;

public abstract class HeatGunItem extends GunItem {

    public static final float MAX_HEAT = 100;

    private final float heatPerShot;

    protected HeatGunItem(Properties pProperties, int cooldown, int energyUsage, int reloadTime, float heatPerShot) {
        super(pProperties,
                cooldown, energyUsage, reloadTime);
        this.heatPerShot = heatPerShot;
    }

    @Override
    public boolean canInstallAddon(ItemStack gunStack, ItemStack addonStack) {
        return addonStack.getItem().getClass().equals(HeatPerShotChipItem.class);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if(entity instanceof ServerPlayer player) {
            float heat = getHeat(stack);
            if (heat > 0) {
                if(!isShooting(stack)) {
                    setHeat(stack, heat - 0.25F);
                }
                if (heat < 0.01) {
                    setHeat(stack, 0F);
                }
                if (heat > MAX_HEAT) {
                    tryStartReload(stack, player);
                }
            }
        }
    }

    @Override
    public void addStatsTooltip(ItemStack stack, List<Component> tooltipComponents) {
        super.addStatsTooltip(stack, tooltipComponents);
        tooltipComponents.add(
                Component.literal(" ").append(Component.translatable("mekanism_weaponry.tooltip.gun_heat_per_shot")).append(": ").withStyle(Style.EMPTY.withColor(0x89c98d)).append(
                        Component.literal(String.format("%f heat", getHeatPerShot(stack))).withStyle(Style.EMPTY.withColor(0xFFFFFF))
                )
        );
    }

    @Override
    public boolean isReady(ItemStack stack, ServerPlayer player, Level level) {
        return super.isReady(stack, player, level) && !isOverheated(stack);
    }

    public float getHeatPerShot(ItemStack stack) {
        return Math.max(0, this.heatPerShot/(1+getAddonMultiplier(stack, HeatPerShotChipItem.class)));
    }

    public float getHeat(ItemStack stack) {
        if(stack.getOrCreateTag().contains(ItemTags.HEAT)) {
            return stack.getOrCreateTag().getFloat(ItemTags.HEAT);
        }
        stack.getOrCreateTag().putFloat(ItemTags.HEAT, 0F);
        return getHeat(stack);
    }

    public void setHeat(ItemStack stack, float heat) {
        stack.getOrCreateTag().putFloat(ItemTags.HEAT, heat);
    }

    public boolean isOverheated(ItemStack stack) {
        return getHeat(stack) > MAX_HEAT;
    }

    private static final int[] COLORS = new int[]{
            0xFF49ef1f, 0xFF58dd1e, 0xFF66ca1d, 0xFF75b81c,
            0xFF83a61b, 0xFF92931a, 0xFFa08119, 0xFFaf6f18,
            0xFFbd5c17, 0xFFcb4a16, 0xFFda3815, 0xFFe92514,
            0xFFf71313
    };

    public static IItemDecorator decorator() {
        return (guiGraphics, font, stack, xOffset, yOffset) -> {
            HeatGunItem item = (HeatGunItem) stack.getItem();
            float f = Math.min(1F, item.getHeat(stack)/HeatGunItem.MAX_HEAT);
            for (int i = 0; i < MAX_BAR_WIDTH * f; i++) {
                guiGraphics.fill(RenderType.guiOverlay(), xOffset+2+i, yOffset+13, xOffset+2+i+1, yOffset+13+1, COLORS[i]);
                guiGraphics.fill(RenderType.guiOverlay(), xOffset+2+i, yOffset+14, xOffset+2+i+1, yOffset+14+1, 0xFF000000);
            }
            return true;
        };
    }

}
