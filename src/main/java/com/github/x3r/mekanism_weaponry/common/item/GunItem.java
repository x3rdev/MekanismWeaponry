package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.common.item.addon.EnergyUsageChipItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.FireRateChipItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.GunAddonItem;
import com.github.x3r.mekanism_weaponry.common.packet.MekanismWeaponryPacketHandler;
import com.github.x3r.mekanism_weaponry.common.packet.ReloadGunClientPacket;
import com.github.x3r.mekanism_weaponry.common.packet.ReloadGunServerPacket;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GunItem extends Item {

    private final int cooldown;
    private final int energyUsage;
    private final int reloadTime;

    protected GunItem(Properties pProperties, int cooldown, int energyUsage, int reloadTime) {
        super(pProperties.stacksTo(1).setNoRepair().rarity(Rarity.UNCOMMON));
        this.cooldown = cooldown;
        this.energyUsage = energyUsage;
        this.reloadTime = reloadTime;

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!getAddon(stack, 3).isEmpty()) {
            setScoping(stack, !isScoping(stack));
            player.getCooldowns().addCooldown(stack.getItem(), 5);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(!level.isClientSide() && !isSelected) {
            serverStoppedShooting(stack);
        }
    }

    public void tryStartReload(ItemStack stack, ServerPlayer player) {
        if(stack.getItem() instanceof GunItem item) {
            if(canReload(stack, player)) {
                item.serverReload(stack, player);
                MekanismWeaponryPacketHandler.sendToClient(new ReloadGunClientPacket(), player);
            }
            if(!player.getCooldowns().isOnCooldown(stack.getItem())) {
                player.serverLevel().playSound(null, player.getEyePosition().x, player.getEyePosition().y, player.getEyePosition().z, SoundRegistry.GUN_OUT_OF_AMMO.get(), SoundSource.PLAYERS, 1.5F, 1.0F);
            }
        }
    }

    public boolean canReload(ItemStack stack, ServerPlayer serverPlayer) {
        return !serverPlayer.getCooldowns().isOnCooldown(stack.getItem());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(
                Component.translatable("mekanism_weaponry.tooltip.gun_energy").withStyle(Style.EMPTY.withColor(0x2fb2d6)).append(
                        Component.literal(String.format("%d/%d FE", getEnergyStorage(pStack).getEnergyStored(), getEnergyStorage(pStack).getMaxEnergyStored())).withStyle(Style.EMPTY.withColor(0xFFFFFF))
                )
        );

        if(!Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("mekanism_weaponry.tooltip.gun_stats")
                    .append(" [SHIFT] ").withStyle(Style.EMPTY.withColor(0x5c5c5c))
            );
        } else {
            addStatsTooltip(pStack, pTooltipComponents);
        }
        if(!Screen.hasControlDown()) {
            pTooltipComponents.add(Component.translatable("mekanism_weaponry.tooltip.gun_addons")
                    .append(" [CTRL] ").withStyle(Style.EMPTY.withColor(0x5c5c5c))
            );
        } else {
            addAddonsTooltip(pStack, pTooltipComponents);
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public void addStatsTooltip(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.translatable("mekanism_weaponry.tooltip.gun_stats").withStyle(Style.EMPTY.withColor(0x5c5c5c)));
        tooltipComponents.add(
                Component.literal(" ").append(Component.translatable("mekanism_weaponry.tooltip.gun_cooldown")).append(": ").withStyle(Style.EMPTY.withColor(0x89c98d)).append(
                        Component.literal(String.format("%d ticks", getCooldown(stack))).withStyle(Style.EMPTY.withColor(0xFFFFFF))
                )
        );
        tooltipComponents.add(
                Component.literal(" ").append(Component.translatable("mekanism_weaponry.tooltip.gun_energy_usage")).append(": ").withStyle(Style.EMPTY.withColor(0x89c98d)).append(
                        Component.literal(String.format("%d / shot", getEnergyUsage(stack))).withStyle(Style.EMPTY.withColor(0xFFFFFF))
                )
        );
        tooltipComponents.add(
                Component.literal(" ").append(Component.translatable("mekanism_weaponry.tooltip.gun_reload_time")).append(": ").withStyle(Style.EMPTY.withColor(0x89c98d)).append(
                        Component.literal(String.format("%d ticks", reloadTime)).withStyle(Style.EMPTY.withColor(0xFFFFFF))
                )
        );
    }

    public void addAddonsTooltip(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.translatable("mekanism_weaponry.tooltip.gun_addons").withStyle(Style.EMPTY.withColor(0x5c5c5c)));
        ItemStack[] addons = new ItemStack[5];
        for (int i = 0; i < addons.length; i++) {
            addons[i] = getAddon(stack, i);
        }
        if(Arrays.stream(addons).allMatch(ItemStack::isEmpty)) {
            tooltipComponents.add(Component.translatable("mekanism_weaponry.tooltip.gun_no_addons"));
        } else {
            for (ItemStack addon : addons) {
                if(!addon.isEmpty()) {
                    tooltipComponents.add(Component.literal(" ").append(addon.getHoverName()));
                }
            }
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.getItem().equals(newStack.getItem());
    }

    public boolean isReady(ItemStack stack, ServerPlayer player, Level level) {
        long tick = level.getGameTime();
        return !player.getCooldowns().isOnCooldown(stack.getItem()) &&
                isOffCooldown(stack, tick) &&
                hasSufficientEnergy(stack);
    }

    public int getCooldown(ItemStack stack) {
        return (int) Math.max(0, this.cooldown - getAddonMultiplier(stack, FireRateChipItem.class));
    }

    public int getEnergyUsage(ItemStack stack) {
        return (int) Math.max(0, this.energyUsage - 10 * getAddonMultiplier(stack, EnergyUsageChipItem.class));
    }

    public int getReloadTime(ItemStack stack) {
        return reloadTime;
    }

    public long getLastShotTick(ItemStack stack) {
        if(stack.getOrCreateTag().contains(ItemTags.LAST_SHOT_TICK)) {
            return stack.getOrCreateTag().getLong(ItemTags.LAST_SHOT_TICK);
        }
        setLastShotTick(stack, 0L);
        return getLastShotTick(stack);
    }

    public void setLastShotTick(ItemStack stack, long tick) {
        stack.getOrCreateTag().putLong(ItemTags.LAST_SHOT_TICK, tick);
    }

    public boolean isOffCooldown(ItemStack stack, long tick) {
        return tick - getLastShotTick(stack) >= getCooldown(stack);
    }

    public IEnergyStorage getEnergyStorage(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(() -> new RuntimeException("Missing energy capability"));
    }

    public boolean hasSufficientEnergy(ItemStack stack) {
        return getEnergyStorage(stack).getEnergyStored() >= energyUsage;
    }

    public ItemStack getAddon(ItemStack stack, int index) {
        switch (index) {
            case 0 -> {
                return getAddon(stack, ItemTags.CHIP1);
            }
            case 1 -> {
                return getAddon(stack, ItemTags.PAINT);
            }
            case 2 -> {
                return getAddon(stack, ItemTags.CHIP2);
            }
            case 3 -> {
                return getAddon(stack, ItemTags.SCOPE);
            }
            case 4 -> {
                return getAddon(stack, ItemTags.CHIP3);
            }
            default -> throw new IllegalArgumentException();
        }
    }

    private ItemStack getAddon(ItemStack stack, String addonSlot) {
        if(stack.getOrCreateTag().contains(addonSlot)) {
            return ItemStack.of(stack.getOrCreateTag().getCompound(addonSlot));
        }
        stack.getOrCreateTag().put(addonSlot, ItemStack.EMPTY.serializeNBT());
        return getAddon(stack, addonSlot);
    }

    public boolean hasAddon(ItemStack stack, Class<? extends GunAddonItem> addonClass) {
        for (int i = 0; i < 5; i++) {
            if(addonClass.isInstance(getAddon(stack, i).getItem())) {
                return true;
            }
        }
        return false;
    }

    public List<ItemStack> getAddonsOfType(ItemStack stack, Class<? extends GunAddonItem> addonClass) {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ItemStack addonStack = getAddon(stack, i);
            if(addonClass.isInstance(addonStack.getItem())) {
                list.add(addonStack);
            }
        }
        return list;
    }

    public float getAddonMultiplier(ItemStack stack, Class<? extends GunAddonItem> addonClass) {
        float count = 0;
        for (int i = 0; i < 5; i++) {
            ItemStack addonStack = getAddon(stack, i);
            if(addonClass.isInstance(addonStack.getItem())) {
                count += ((GunAddonItem) addonStack.getItem()).mul();
            }
        }
        return count;
    }

    public void setAddon(ItemStack stack, ItemStack chipStack, int index) {
        switch (index) {
            case 0 -> stack.getOrCreateTag().put(ItemTags.CHIP1, chipStack.serializeNBT());
            case 1 -> stack.getOrCreateTag().put(ItemTags.PAINT, chipStack.serializeNBT());
            case 2 -> stack.getOrCreateTag().put(ItemTags.CHIP2, chipStack.serializeNBT());
            case 3 -> stack.getOrCreateTag().put(ItemTags.SCOPE, chipStack.serializeNBT());
            case 4 -> stack.getOrCreateTag().put(ItemTags.CHIP3, chipStack.serializeNBT());
            default -> throw new IllegalArgumentException();
        }
    }

    public boolean isShooting(ItemStack stack) {
        if(stack.getOrCreateTag().contains(ItemTags.IS_SHOOTING)) {
            return stack.getOrCreateTag().getBoolean(ItemTags.IS_SHOOTING);
        }
        setShooting(stack, false);
        return isShooting(stack);
    }

    public void setShooting(ItemStack stack, boolean shooting) {
        stack.getOrCreateTag().putBoolean(ItemTags.IS_SHOOTING, shooting);
    }

    public boolean isScoping(ItemStack stack) {
        if(stack.getOrCreateTag().contains(ItemTags.IS_SCOPING)) {
            return stack.getOrCreateTag().getBoolean(ItemTags.IS_SCOPING);
        }
        setScoping(stack, false);
        return isScoping(stack);
    }

    public void setScoping(ItemStack stack, boolean scoping) {
        stack.getOrCreateTag().putBoolean(ItemTags.IS_SCOPING, scoping);
    }

    public abstract void serverShoot(ItemStack stack, ServerPlayer player);

    public abstract void clientShoot(ItemStack stack, Player player);

    public abstract void serverReload(ItemStack stack, ServerPlayer player);

    public abstract void clientReload(ItemStack stack, Player player);

    public void serverStoppedShooting(ItemStack stack){
        setShooting(stack, false);
    }

    public abstract boolean canInstallAddon(ItemStack gunStack, ItemStack addonStack);

}
