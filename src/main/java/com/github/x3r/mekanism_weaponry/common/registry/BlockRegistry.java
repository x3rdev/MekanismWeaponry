package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.block.WeaponWorkbenchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(MekanismWeaponry.MOD_ID);

    public static final DeferredHolder<Block, WeaponWorkbenchBlock> WEAPON_WORKBENCH = BLOCKS.register("weapon_workbench",
            () -> new WeaponWorkbenchBlock(BlockBehaviour.Properties.of()));
}
