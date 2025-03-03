package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.menu.WeaponWorkbenchMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuTypeRegistry {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, MekanismWeaponry.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<WeaponWorkbenchMenu>> WEAPON_WORKBENCH = MENU_TYPES.register("weapon_workbench",
            () -> new MenuType<>(WeaponWorkbenchMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
