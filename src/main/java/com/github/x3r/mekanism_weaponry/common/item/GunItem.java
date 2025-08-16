package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.common.item.addon.EnergyUsageChipItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.FireRateChipItem;
import com.github.x3r.mekanism_weaponry.common.item.addon.GunAddonItem;
import com.github.x3r.mekanism_weaponry.common.packet.ReloadGunPayload;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import net.minecraft.network.chat.Component;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GunItem extends Item {

    private final int cooldown;
    private final int energyUsage;
    private final int reloadTime;

    protected GunItem(Properties pProperties, int cooldown, int energyUsage, int reloadTime) {
        super(pProperties.stacksTo(1).setNoRepair()
                .component(DataComponentRegistry.LAST_SHOT_TICK.get(), 0L)
                .component(DataComponentRegistry.RELOADING, false)
                .component(DataComponentRegistry.ADDONS.get(), new DataComponentAddons())
                .component(DataComponentRegistry.IS_SHOOTING, false)
                .component(DataComponentRegistry.IS_SCOPING, false)
                .rarity(Rarity.UNCOMMON));

        this.cooldown = cooldown;
        this.energyUsage = energyUsage;
        this.reloadTime = reloadTime;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!getAddon(stack, 3).isEmpty()) {
            stack.set(DataComponentRegistry.IS_SCOPING, !stack.get(DataComponentRegistry.IS_SCOPING));
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
                PacketDistributor.sendToPlayer(player, new ReloadGunPayload());
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(
                Component.translatable("mekanism_weaponry.tooltip.gun_energy").withColor(0x2fb2d6).append(
                        Component.literal(String.format("%d/%d FE", getEnergyStorage(stack).getEnergyStored(), getEnergyStorage(stack).getMaxEnergyStored())).withColor(0xFFFFFF)
                )
        );
        if(!tooltipFlag.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("mekanism_weaponry.tooltip.gun_stats")
                    .append(" [SHIFT] ").withColor(0x5c5c5c)
            );
        } else {
            addStatsTooltip(stack, tooltipComponents);
        }
        if(!tooltipFlag.hasControlDown()) {
            tooltipComponents.add(Component.translatable("mekanism_weaponry.tooltip.gun_addons")
                    .append(" [CTRL] ").withColor(0x5c5c5c)
            );
        } else {
            addAddonsTooltip(stack, tooltipComponents);
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public void addStatsTooltip(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.translatable("mekanism_weaponry.tooltip.gun_stats").withColor(0x5c5c5c));
        tooltipComponents.add(
                Component.literal(" ").append(Component.translatable("mekanism_weaponry.tooltip.gun_cooldown")).append(": ").withColor(0x89c98d).append(
                        Component.literal(String.format("%d ticks", getCooldown(stack))).withColor(0xFFFFFF)
                )
        );
        tooltipComponents.add(
                Component.literal(" ").append(Component.translatable("mekanism_weaponry.tooltip.gun_energy_usage")).append(": ").withColor(0x89c98d).append(
                        Component.literal(String.format("%d / shot", getEnergyUsage(stack))).withColor(0xFFFFFF)
                )
        );
        tooltipComponents.add(
                Component.literal(" ").append(Component.translatable("mekanism_weaponry.tooltip.gun_reload_time")).append(": ").withColor(0x89c98d).append(
                        Component.literal(String.format("%d ticks", reloadTime)).withColor(0xFFFFFF)
                )
        );
    }

    public void addAddonsTooltip(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.translatable("mekanism_weaponry.tooltip.gun_addons").withColor(0x5c5c5c));
        DataComponentAddons addons = stack.get(DataComponentRegistry.ADDONS.get());
        if(Arrays.stream(addons.getAddons()).allMatch(s -> s.equals(ItemStack.EMPTY))) {
            tooltipComponents.add(Component.translatable("mekanism_weaponry.tooltip.gun_no_addons"));
        } else {
            for (ItemStack addon : addons.getAddons()) {
                if(!addon.equals(ItemStack.EMPTY)) {
                    tooltipComponents.add(Component.literal(" ").append(addon.getHoverName()));
                }
            }
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
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
        return stack.get(DataComponentRegistry.LAST_SHOT_TICK.get());
    }

    public void setLastShotTick(ItemStack stack, long tick) {
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

    public ItemStack getAddon(ItemStack stack, int index) {
        DataComponentAddons addons = stack.get(DataComponentRegistry.ADDONS.get());
        return addons.getAddon(index);
    }

    public boolean hasAddon(ItemStack stack, Class<? extends GunAddonItem> addon) {
        DataComponentAddons addons = stack.get(DataComponentRegistry.ADDONS.get());
        for (int i = 0; i < 5; i++) {
            if(addon.isInstance(addons.getAddon(i).getItem())) {
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
        DataComponentAddons addons = stack.get(DataComponentRegistry.ADDONS.get());
        for (int i = 0; i < 5; i++) {
            if(addonClass.isInstance(addons.getAddon(i).getItem())) {
                count += ((GunAddonItem) addons.getAddon(i).getItem()).mul();
            }
        }
        return count;
    }

    public void setAddon(ItemStack stack, ItemStack chipStack, int index) {
        DataComponentAddons addons = stack.get(DataComponentRegistry.ADDONS.get());
        ItemStack[] addonArr = new ItemStack[5];
        for (int i = 0; i < addonArr.length; i++) {
            addonArr[i] = addons.getAddon(i);
        }
        addonArr[index] = chipStack;
        stack.set(DataComponentRegistry.ADDONS.get(), new DataComponentAddons(
                addonArr[0],
                addonArr[1],
                addonArr[2],
                addonArr[3],
                addonArr[4]
        ));
    }

    public boolean isShooting(ItemStack stack) {
        return stack.get(DataComponentRegistry.IS_SHOOTING);
    }

    public abstract void serverShoot(ItemStack stack, ServerPlayer player);

    public abstract void clientShoot(ItemStack stack, Player player);

    public abstract void serverReload(ItemStack stack, ServerPlayer player);

    public abstract void clientReload(ItemStack stack, Player player);

    public void serverStoppedShooting(ItemStack stack){
        stack.set(DataComponentRegistry.IS_SHOOTING, false);
    }

    public abstract boolean canInstallAddon(ItemStack gunStack, ItemStack addonStack);

    public record DataComponentAddons(ItemStack chip1, ItemStack paint, ItemStack chip2, ItemStack scope, ItemStack chip3) {
        public DataComponentAddons() {
            this(
                    ItemStack.EMPTY,
                    ItemStack.EMPTY,
                    ItemStack.EMPTY,
                    ItemStack.EMPTY,
                    ItemStack.EMPTY
            );
        }

        public ItemStack[] getAddons() {
            return new ItemStack[]{chip1, paint, chip2, scope, chip3};
        }

        public ItemStack getAddon(int index) {
            switch (index) {
                case 0 -> {
                    return chip1;
                }
                case 1 -> {
                    return paint;
                }
                case 2 -> {
                    return chip2;
                }
                case 3 -> {
                    return scope;
                }
                case 4 -> {
                    return chip3;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
