package com.github.x3r.mekanism_weaponry.common.menu;

import com.github.x3r.mekanism_weaponry.common.item.GunChipItem;
import com.github.x3r.mekanism_weaponry.common.item.GunItem;
import com.github.x3r.mekanism_weaponry.common.registry.BlockRegistry;
import com.github.x3r.mekanism_weaponry.common.registry.MenuTypeRegistry;
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
        this.addSlot(new Slot(container, 0, 8, 16) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof GunItem;
            }
        });
        this.addSlot(new ChipSlot(container, 1, 152, 16));
        this.addSlot(new Slot(container, 2, 8, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false; // TODO make this slot only take gun skin items
            }
        });
        this.addSlot(new ChipSlot(container, 3, 152, 35));
        this.addSlot(new Slot(container, 4, 8, 54) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false; // TODO make this slot only take paints
            }
        });
        this.addSlot(new ChipSlot(container, 5, 152, 54));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
    
    private Container createContainer() {
        Container c = new SimpleContainer(6) {
            @Override
            public void setChanged() {
                super.setChanged();
                WeaponWorkbenchMenu.this.slotsChanged(this);
            }
        };
        return c;
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
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

    private static class WeaponWorkbenchSlot extends Slot {
        public WeaponWorkbenchSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public void setChanged() {
            super.setChanged();
        }
    }

    private static class ChipSlot extends Slot {
        public ChipSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof GunChipItem;
        }
    }
}
