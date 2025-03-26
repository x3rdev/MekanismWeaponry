package com.github.x3r.mekanism_weaponry.common.registry;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.github.x3r.mekanism_weaponry.common.menu.WeaponWorkbenchMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeRegistry {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MekanismWeaponry.MOD_ID);

    public static final RegistryObject<MenuType<WeaponWorkbenchMenu>> WEAPON_WORKBENCH = MENU_TYPES.register("weapon_workbench",
            () -> new MenuType<>(WeaponWorkbenchMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
