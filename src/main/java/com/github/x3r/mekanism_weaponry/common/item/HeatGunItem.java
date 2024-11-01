package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.IItemDecorator;

public abstract class HeatGunItem extends GunItem {

    public static final float MAX_HEAT = 100;

    protected final float heatPerShot;

    public HeatGunItem(Properties pProperties, int cooldown, int energyUsage, float heatPerShot) {
        super(pProperties, cooldown, energyUsage);
        this.heatPerShot = heatPerShot;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(entity instanceof ServerPlayer player) {
            float heat = getHeat(stack);
            if (heat > 0) {
                setHeat(stack, heat - 0.25F);
                if (heat < 0.01) {
                    setHeat(stack, 0F);
                }
                if (heat > MAX_HEAT) {
                    tryStartReload(stack, player);
                }
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public boolean isReady(ItemStack stack, Level level) {
        return super.isReady(stack, level) && !isOverheated(stack);
    }

    public float getHeat(ItemStack stack) {
        return stack.get(DataComponentRegistry.HEAT.get()).floatValue();
    }

    public void setHeat(ItemStack stack, float heat) {
        stack.set(DataComponentRegistry.HEAT.get(), heat);
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
        return new IItemDecorator() {

            @Override
            public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
                HeatGunItem item = (HeatGunItem) stack.getItem();
                float f = Math.min(1F, item.getHeat(stack)/HeatGunItem.MAX_HEAT);
                for (int i = 0; i < MAX_BAR_WIDTH * f; i++) {
                    guiGraphics.fill(RenderType.guiOverlay(), xOffset+2+i, yOffset+13, xOffset+2+i+1, yOffset+13+1, COLORS[i]);
                    guiGraphics.fill(RenderType.guiOverlay(), xOffset+2+i, yOffset+14, xOffset+2+i+1, yOffset+14+1, 0xFF000000);
                }

                return true;
            }
        };
    }

}
