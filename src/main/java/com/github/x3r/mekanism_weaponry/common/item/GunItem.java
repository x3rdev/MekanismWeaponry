package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.common.packet.ReloadGunPayload;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.github.x3r.mekanism_weaponry.common.scheduler.Scheduler;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.IItemDecorator;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class GunItem extends Item {

    public static final float MAX_HEAT = 100;
    protected final float heatPerShot;
    protected final int cooldown;
    protected final int energyUsage;

    protected GunItem(Properties pProperties, float heatPerShot, int cooldown, int energyUsage) {
        super(pProperties.stacksTo(1).setNoRepair()
                .component(DataComponentRegistry.LAST_SHOT_TICK.get(), 0L)
                .component(DataComponentRegistry.HEAT.get(), 0F)
                .component(DataComponentRegistry.RELOADING, false)
                .component(DataComponentRegistry.CHIPS.get(), new DataComponentChips(new ArrayList<>())));
        this.heatPerShot = heatPerShot;
        this.cooldown = cooldown;
        this.energyUsage = energyUsage;
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

    public void tryStartReload(ItemStack stack, ServerPlayer player) {
        if(stack.getItem() instanceof GunItem item) {
            item.serverReload(stack, item, player);
            PacketDistributor.sendToPlayer(player, new ReloadGunPayload());
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal(String.format("%d/%d FE", getEnergyStorage(stack).getEnergyStored(), getEnergyStorage(stack).getMaxEnergyStored())));
        if(!getChips(stack).isEmpty()) {
            tooltipComponents.add(Component.literal(String.format("%d chips installed [SHIFT]", getChips(stack).size())));
            if(tooltipFlag.hasShiftDown()) {
                tooltipComponents.removeLast();
                for (ItemStack chip : getChips(stack)) {
                    GunChipItem gunChipItem = (GunChipItem) chip.getItem();
                    tooltipComponents.add(Component.literal(gunChipItem.getChipType().getId()));
                }
            }
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return false;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        return true;
    }


    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.getItem().equals(newStack.getItem());
    }

    public boolean isReady(ItemStack stack, Level level) {
        long tick = level.getGameTime();
        return isOffCooldown(stack, tick) && hasSufficientEnergy(stack) && !isOverheated(stack);
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

    public int getCooldown(ItemStack stack) {
        return ((GunItem) stack.getItem()).cooldown;
    }

    public long getLastShotTick(ItemStack stack) {
        return stack.get(DataComponentRegistry.LAST_SHOT_TICK.get()).longValue();
    }

    public void setListShotTick(ItemStack stack, long tick) {
        stack.set(DataComponentRegistry.LAST_SHOT_TICK.get(), tick);
    }

    public boolean isOffCooldown(ItemStack stack, long tick) {
        return tick - getLastShotTick(stack) >= getCooldown(stack);
    }

    public IEnergyStorage getEnergyStorage(ItemStack stack) {
        return stack.getCapability(Capabilities.EnergyStorage.ITEM);
    }

    public boolean hasSufficientEnergy(ItemStack stack) {
        return getEnergyStorage(stack).getEnergyStored() >= energyUsage;
    }

    public void setReloading(ItemStack stack, boolean b) {
        stack.set(DataComponentRegistry.RELOADING.get(), b);
    }

    public boolean isReloading(ItemStack stack) {
        return stack.get(DataComponentRegistry.RELOADING.get()).booleanValue();
    }

    public ImmutableList<ItemStack> getChips(ItemStack stack) {
        return ImmutableList.copyOf(stack.get(DataComponentRegistry.CHIPS.get()).chips());
    }

    public void addChip(ItemStack gunStack, ItemStack chipStack) {
        List<ItemStack> copyList = new ArrayList<>(gunStack.get(DataComponentRegistry.CHIPS.get()).chips());
        copyList.add(chipStack);
        gunStack.set(DataComponentRegistry.CHIPS.get(), new DataComponentChips(copyList));
    }

    public void clearChips(ItemStack gunStack) {
        gunStack.set(DataComponentRegistry.CHIPS.get(), new DataComponentChips(new ArrayList<>()));
    }

    public boolean containsChip(ItemStack stack, GunChipItem.ChipType chipType) {
        return getChips(stack).contains(chipType);
    }

    public int getChipCount(ItemStack stack, GunChipItem.ChipType chipType) {
        int i = 0;
        for (ItemStack chip : getChips(stack)) {
            if(((GunChipItem) chip.getItem()).getChipType().equals(chipType)) i++;
        }
        return i;
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
                GunItem item = (GunItem) stack.getItem();
                float f = Math.min(1F, item.getHeat(stack)/GunItem.MAX_HEAT);
                for (int i = 0; i < MAX_BAR_WIDTH * f; i++) {
                    guiGraphics.fill(RenderType.guiOverlay(), xOffset+2+i, yOffset+13, xOffset+2+i+1, yOffset+13+1, COLORS[i]);
                    guiGraphics.fill(RenderType.guiOverlay(), xOffset+2+i, yOffset+14, xOffset+2+i+1, yOffset+14+1, 0xFF000000);
                }

                return true;
            }
        };
    }

    public abstract void serverShoot(ItemStack stack, GunItem item, ServerPlayer player);

    public abstract void clientShoot(ItemStack stack, GunItem item, LocalPlayer player);

    public abstract void serverReload(ItemStack stack, GunItem item, ServerPlayer player);

    public abstract void clientReload(ItemStack stack, GunItem item, LocalPlayer player);

    public abstract boolean canInstallChip(ItemStack gunStack, ItemStack chipStack);

    public record DataComponentChips(List<ItemStack> chips){}

}
