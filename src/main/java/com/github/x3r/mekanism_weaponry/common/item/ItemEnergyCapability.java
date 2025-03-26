package com.github.x3r.mekanism_weaponry.common.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemEnergyCapability implements ICapabilityProvider, IEnergyStorage {
    private final ItemStack itemStack;
    private final EnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> lazyEnergyStorage;

    public ItemEnergyCapability(ItemStack itemStack, EnergyStorage energyStorage) {
        this.itemStack = itemStack;
        this.energyStorage = energyStorage;

        if(itemStack.getOrCreateTag().contains(ItemTags.ENERGY)) {
            this.energyStorage.deserializeNBT(itemStack.getOrCreateTag().get(ItemTags.ENERGY));
        }
        lazyEnergyStorage = LazyOptional.of(() -> this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyStorage.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int ret = energyStorage.receiveEnergy(maxReceive, simulate);

        if(!simulate)
            itemStack.getOrCreateTag().put(ItemTags.ENERGY, energyStorage.serializeNBT());

        return ret;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int ret = energyStorage.extractEnergy(maxExtract, simulate);

        if(!simulate)
            itemStack.getOrCreateTag().put(ItemTags.ENERGY, energyStorage.serializeNBT());

        return ret;
    }

    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return energyStorage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return energyStorage.canReceive();
    }
}
