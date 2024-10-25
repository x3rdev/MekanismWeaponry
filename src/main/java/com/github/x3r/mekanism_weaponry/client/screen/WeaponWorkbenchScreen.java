package com.github.x3r.mekanism_weaponry.client.screen;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.menu.WeaponWorkbenchMenu;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class WeaponWorkbenchScreen extends AbstractContainerScreen<WeaponWorkbenchMenu> {

    private static final ResourceLocation MENU_RESOURCE = ResourceLocation.fromNamespaceAndPath(MekanismWeaponry.MOD_ID, "textures/gui/container/weapon_workbench.png");


    public WeaponWorkbenchScreen(WeaponWorkbenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(this.MENU_RESOURCE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if(getMenu().getSlot(0).hasItem()) {
            renderItemPreview(guiGraphics, getMenu().getSlot(0).getItem(), mouseX, mouseY);
        }
    }

    private void renderItemPreview(GuiGraphics guiGraphics, ItemStack stack, int mouseX, int mouseY) {
        guiGraphics.pose().pushPose();

        guiGraphics.pose().translate(guiGraphics.guiWidth()/2F, guiGraphics.guiHeight()/2.4F, 100);
        guiGraphics.pose().scale(20.0F, -20.0F, 20.0F);

        float xMouseAngle = -(float)Math.atan((guiGraphics.guiWidth()/2F - mouseX)/150.0F);
        float yMouseAngle = -(float)Math.atan((guiGraphics.guiHeight()/2.5F - mouseY)/150.0F);
        guiGraphics.pose().mulPose(Axis.YP.rotation(Mth.HALF_PI*xMouseAngle+Mth.HALF_PI));
        guiGraphics.pose().mulPose(Axis.ZP.rotation(Mth.HALF_PI*yMouseAngle));

        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.NONE,
                15728880,
                OverlayTexture.NO_OVERLAY,
                guiGraphics.pose(),
                guiGraphics.bufferSource(),
                Minecraft.getInstance().level,
                0);
        guiGraphics.pose().popPose();

    }
}
