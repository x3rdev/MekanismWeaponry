package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.block.WeaponWorkbenchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MekanismWeaponry.MOD_ID);

    public static final RegistryObject<WeaponWorkbenchBlock> WEAPON_WORKBENCH = BLOCKS.register("weapon_workbench",
            () -> new WeaponWorkbenchBlock(BlockBehaviour.Properties.of()));
}
