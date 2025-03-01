package com.github.x3r.mekanism_weaponry;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MekanismWeaponryConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.IntValue plasmaRifleCooldown;
    public static final ModConfigSpec.IntValue plasmaRifleEnergyUsage;
    public static final ModConfigSpec.IntValue plasmaRifleReloadTime;
    public static final ModConfigSpec.IntValue plasmaRifleHeatPerShot;

    static {
        BUILDER.push("Mekanism Weaponry Config");

        plasmaRifleCooldown = BUILDER.comment("Cooldown in ticks between when gun can be shot").defineInRange("plasmaRifleCooldown", 6, 0, Integer.MAX_VALUE);
        plasmaRifleEnergyUsage = BUILDER.comment("How much FE should be drained from the guns battery every shot").defineInRange("plasmaRifleEnergyUsage", 250, 0, Integer.MAX_VALUE);
        plasmaRifleReloadTime = BUILDER.comment("How many ticks it should take to reload the weapon").defineInRange("plasmaRifleReloadTime", 45, 0, Integer.MAX_VALUE);
        plasmaRifleHeatPerShot = BUILDER.comment("How much heat should accumulate every shot").defineInRange("plasmaRifleHeatPerShot", 20, 0, Integer.MAX_VALUE);



        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
