package com.github.x3r.mekanism_weaponry.common.menu;

import com.github.x3r.mekanism_weaponry.common.item.GunAddonItem;
import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.github.x3r.mekanism_weaponry.common.registry.BlockRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.MenuTypeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WeaponWorkbenchMenu extends AbstractContainerMenu {

    private final Inventory playerInventory;
    private final ContainerLevelAccess access;
    private final Container container;

    public WeaponWorkbenchMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }
    public WeaponWorkbenchMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuTypeRegistry.WEAPON_WORKBENCH.get(), containerId);
        this.playerInventory = playerInventory;
        this.access = access;
        this.container = createContainer();
        this.addSlot(new GunSlot(container, 0, 8, 16));
        this.addSlot(new AddonSlot(container, 1, 152, 16, GunAddonItem.AddonType.CHIP));
        this.addSlot(new AddonSlot(container, 2, 8, 35, GunAddonItem.AddonType.PAINT));
        this.addSlot(new AddonSlot(container, 3, 152, 35, GunAddonItem.AddonType.CHIP));
        this.addSlot(new AddonSlot(container, 4, 8, 54, GunAddonItem.AddonType.SCOPE));
        this.addSlot(new AddonSlot(container, 5, 152, 54, GunAddonItem.AddonType.CHIP));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if(container == this.container) {

        }
    }

    public void onItemSet(int index, ItemStack itemStack) {
        if(index == 0) {
            if(itemStack.getItem() instanceof GunItem gunItem) {
                for (int i = 1; i < 6; i++) {
                    this.container.setItem(i, gunItem.getAddon(itemStack, i-1).copy());
                }
            } else {
                for (int i = 1; i < 6; i++) {
                    this.container.setItem(i, ItemStack.EMPTY);
                }
            }
        } else {
            ItemStack gunStack = container.getItem(0);
            if(gunStack.getItem() instanceof GunItem gunItem) {
                gunItem.setAddon(gunStack, itemStack, index-1);
            }
        }
    }

    private Container createContainer() {
        Container c = new SimpleContainer(6) {
            @Override
            public void setChanged() {
                super.setChanged();
                WeaponWorkbenchMenu.this.slotsChanged(this);
            }

            @Override
            public void setItem(int index, ItemStack stack) {
                super.setItem(index, stack);
                WeaponWorkbenchMenu.this.onItemSet(index, stack);
            }

        };
        return c;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((p_39796_, p_39797_) -> this.clearContainer(player, this.container));
    }

    @Override
    protected void clearContainer(Player player, Container container) {
        if (!player.isAlive() || player instanceof ServerPlayer && ((ServerPlayer)player).hasDisconnected()) {
            player.drop(container.removeItemNoUpdate(0), false);
        } else {
            Inventory inventory = player.getInventory();
            if (inventory.player instanceof ServerPlayer) {
                inventory.placeItemBackInInventory(container.removeItemNoUpdate(0));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 6) {
                if (!this.moveItemStackTo(itemstack1, 6, 42, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 6, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, BlockRegistry.WEAPON_WORKBENCH.get());
    }

    private static class GunSlot extends Slot {

        public GunSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof GunItem;
        }
    }
    
    private static class AddonSlot extends Slot {

        private final GunAddonItem.AddonType addonType;

        public AddonSlot(Container container, int slot, int x, int y, GunAddonItem.AddonType addonType) {
            super(container, slot, x, y);
            this.addonType = addonType;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof GunAddonItem gunAddonItem &&
                    gunAddonItem.getAddonType().equals(this.addonType) &&
                    !container.getItem(0).isEmpty() &&
                    container.getItem(0).getItem() instanceof GunItem gunItem &&
                    gunItem.canInstallAddon(container.getItem(0), stack);
        }
    }

}
