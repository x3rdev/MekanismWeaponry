package com.github.x3r.mekanism_weaponry.common.item;

import com.github.x3r.mekanism_weaponry.common.packet.ReloadGunPayload;
import com.github.x3r.mekanism_weaponry.common.registry.DataComponentRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Objects;

public abstract class GunItem extends Item {

    protected final int cooldown;
    protected final int energyUsage;

    public GunItem(Properties pProperties, int cooldown, int energyUsage) {
        super(pProperties.stacksTo(1).setNoRepair()
                .component(DataComponentRegistry.LAST_SHOT_TICK.get(), 0L)
                .component(DataComponentRegistry.RELOADING, false)
                .component(DataComponentRegistry.ADDONS.get(), new DataComponentAddons())
                .component(DataComponentRegistry.IS_SHOOTING, false));

        this.cooldown = cooldown;
        this.energyUsage = energyUsage;
    }

    public void tryStartReload(ItemStack stack, ServerPlayer player) {
        if(stack.getItem() instanceof GunItem item && canReload(stack, player)) {
            item.serverReload(stack, player);
            PacketDistributor.sendToPlayer(player, new ReloadGunPayload());
        }
    }

    public boolean canReload(ItemStack stack, ServerPlayer serverPlayer) {
        return !isReloading(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal(String.format("%d/%d FE", getEnergyStorage(stack).getEnergyStored(), getEnergyStorage(stack).getMaxEnergyStored())));
//        if(!getChips(stack).isEmpty()) {
//            tooltipComponents.add(Component.literal(String.format("%d chips installed [SHIFT]", getChips(stack).size())));
//            if(tooltipFlag.hasShiftDown()) {
//                tooltipComponents.removeLast();
//                for (ItemStack chip : getChips(stack)) {
//                    GunChipItem gunChipItem = (GunChipItem) chip.getItem();
//                    tooltipComponents.add(Component.literal(gunChipItem.getChipType().getId()));
//                }
//            }
//        }
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
        return !isReloading(stack) &&
                isOffCooldown(stack, tick) &&
                hasSufficientEnergy(stack);
    }

    public int getCooldown(ItemStack stack) {
        int cooldown = ((GunItem) stack.getItem()).cooldown - hasAddon(stack, ItemRegistry.FIRE_RATE_CHIP.get());
        return Math.max(0, cooldown);
    }

    public long getLastShotTick(ItemStack stack) {
        return stack.get(DataComponentRegistry.LAST_SHOT_TICK.get()).longValue();
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

    public void setReloading(ItemStack stack, boolean b) {
        stack.set(DataComponentRegistry.RELOADING.get(), b);
    }

    public boolean isReloading(ItemStack stack) {
        return stack.get(DataComponentRegistry.RELOADING.get()).booleanValue();
    }

    public ItemStack getAddon(ItemStack stack, int index) {
        DataComponentAddons addons = stack.get(DataComponentRegistry.ADDONS.get());
        return addons.getAddon(index);
    }

    public int hasAddon(ItemStack stack, Item addon) {
        int count = 0;
        DataComponentAddons addons = stack.get(DataComponentRegistry.ADDONS.get());
        for (int i = 0; i < 5; i++) {
            if(addons.getAddon(i).is(addon)) {
                count++;
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

    public void serverStoppedShooting(ItemStack stack, Player player){
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
