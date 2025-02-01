package com.github.x3r.mekanism_weaponry;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MekanismWeaponryConfig {

    public static final MekanismWeaponryConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    private MekanismWeaponryConfig(ModConfigSpec.Builder builder) {
        
    }

    static {
        Pair<MekanismWeaponryConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(MekanismWeaponryConfig::new);

        //Store the resulting values
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}
