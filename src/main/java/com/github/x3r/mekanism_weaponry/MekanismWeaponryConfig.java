package com.github.x3r.mekanism_weaponry;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MekanismWeaponryConfig {

    public static final MekanismWeaponryConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    private final ModConfigSpec.IntValue plasmaRifleCooldown;
    private final ModConfigSpec.IntValue plasmaRifleEnergyUsage;
    private final ModConfigSpec.IntValue plasmaRifleReloadTime;
    private final ModConfigSpec.IntValue plasmaRifleHeatPerShot;
    private final ModConfigSpec.IntValue plasmaRifleEnergyCapacity;
    private final ModConfigSpec.IntValue plasmaRifleEnergyTransfer;

    private final ModConfigSpec.IntValue railgunCooldown;
    private final ModConfigSpec.IntValue railgunEnergyUsage;
    private final ModConfigSpec.IntValue railgunReloadTime;
    private final ModConfigSpec.IntValue railgunMaxAmmo;
    private final ModConfigSpec.IntValue railgunEnergyCapacity;
    private final ModConfigSpec.IntValue railgunEnergyTransfer;

    private final ModConfigSpec.IntValue teslaMinigunCooldown;
    private final ModConfigSpec.IntValue teslaMinigunEnergyUsage;
    private final ModConfigSpec.IntValue teslaMinigunReloadTime;
    private final ModConfigSpec.IntValue teslaMinigunHeatPerShot;
    private final ModConfigSpec.IntValue teslaMinigunEnergyCapacity;
    private final ModConfigSpec.IntValue teslaMinigunEnergyTransfer;

    private final ModConfigSpec.IntValue gauntletEnergyUsage;
    private final ModConfigSpec.IntValue gauntletEnergyCapacity;
    private final ModConfigSpec.IntValue gauntletEnergyTransfer;

    static {
        Pair<MekanismWeaponryConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(MekanismWeaponryConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    public MekanismWeaponryConfig(ModConfigSpec.Builder builder) {
        builder.push("Mekanism Weaponry Config");

        plasmaRifleCooldown = builder.comment("Cooldown in ticks between when gun can be shot").defineInRange("plasmaRifleCooldown", 6, 0, Integer.MAX_VALUE);
        plasmaRifleEnergyUsage = builder.comment("How much FE should be drained from the guns battery every shot").defineInRange("plasmaRifleEnergyUsage", 250, 0, Integer.MAX_VALUE);
        plasmaRifleReloadTime = builder.comment("How many ticks it should take to reload the weapon").defineInRange("plasmaRifleReloadTime", 45, 0, Integer.MAX_VALUE);
        plasmaRifleHeatPerShot = builder.comment("How much heat should accumulate every shot").defineInRange("plasmaRifleHeatPerShot", 20, 0, Integer.MAX_VALUE);
        plasmaRifleEnergyCapacity = builder.comment("How much energy the gun can store").defineInRange("plasmaRifleEnergyCapacity", 10000, 0, Integer.MAX_VALUE);
        plasmaRifleEnergyTransfer = builder.comment("How fast the gun battery can charge/discharge").defineInRange("plasmaRifleEnergyTransfer", 1000, 0, Integer.MAX_VALUE);

        railgunCooldown = builder.comment("Cooldown in ticks between when gun can be shot").defineInRange("railgunCooldown", 20, 0, Integer.MAX_VALUE);
        railgunEnergyUsage = builder.comment("How much FE should be drained from the guns battery every shot").defineInRange("railgunEnergyUsage", 1000, 0, Integer.MAX_VALUE);
        railgunReloadTime = builder.comment("How many ticks it should take to reload the weapon").defineInRange("railgunReloadTime", 55, 0, Integer.MAX_VALUE);
        railgunMaxAmmo = builder.comment("Max ammo the gun can hold").defineInRange("railgunMaxAmmo", 5, 1, Integer.MAX_VALUE);
        railgunEnergyCapacity = builder.comment("How much energy the gun can store").defineInRange("railgunEnergyCapacity", 100000, 0, Integer.MAX_VALUE);
        railgunEnergyTransfer = builder.comment("How fast the gun battery can charge/discharge").defineInRange("railgunEnergyTransfer", 10000, 0, Integer.MAX_VALUE);

        teslaMinigunCooldown = builder.comment("Cooldown in ticks between when gun can be shot").defineInRange("teslaMinigunCooldown", 4, 0, Integer.MAX_VALUE);
        teslaMinigunEnergyUsage = builder.comment("How much FE should be drained from the guns battery every shot").defineInRange("teslaMinigunEnergyUsage", 1000, 0, Integer.MAX_VALUE);
        teslaMinigunReloadTime = builder.comment("How many ticks it should take to reload the weapon").defineInRange("teslaMinigunReloadTime", 100, 0, Integer.MAX_VALUE);
        teslaMinigunHeatPerShot = builder.comment("How much heat should accumulate every shot").defineInRange("teslaMinigunHeatPerShot", 5, 0, Integer.MAX_VALUE);
        teslaMinigunEnergyCapacity = builder.comment("How much energy the gun can store").defineInRange("teslaMinigunEnergyCapacity", 100000, 0, Integer.MAX_VALUE);
        teslaMinigunEnergyTransfer = builder.comment("How fast the gun battery can charge/discharge").defineInRange("teslaMinigunEnergyTransfer", 10000, 0, Integer.MAX_VALUE);

        gauntletEnergyUsage = builder.comment("How much FE should be drained from the battery every shot").defineInRange("gauntletEnergyUsage", 10000, 0, Integer.MAX_VALUE);
        gauntletEnergyCapacity = builder.comment("How much energy the gauntlet battery can store").defineInRange("gauntletEnergyCapacity", 10000, 0, Integer.MAX_VALUE);
        gauntletEnergyTransfer = builder.comment("How fast the gauntlet battery can charge/discharge").defineInRange("gauntletEnergyTransfer", 1000, 0, Integer.MAX_VALUE);

        builder.pop();
    }


    public int getPlasmaRifleCooldown() {
        if(!CONFIG_SPEC.isLoaded()) {
            return plasmaRifleCooldown.getDefault();
        }
        return plasmaRifleCooldown.get();
    }

    public int getPlasmaRifleEnergyUsage() {
        if(!CONFIG_SPEC.isLoaded()) {
            return plasmaRifleEnergyUsage.getDefault();
        }
        return plasmaRifleEnergyUsage.get();
    }

    public int getPlasmaRifleReloadTime() {
        if(!CONFIG_SPEC.isLoaded()) {
            return plasmaRifleReloadTime.getDefault();
        }
        return plasmaRifleReloadTime.get();
    }

    public int getPlasmaRifleHeatPerShot() {
        if(!CONFIG_SPEC.isLoaded()) {
            return plasmaRifleHeatPerShot.getDefault();
        }
        return plasmaRifleHeatPerShot.get();
    }

    public int getPlasmaRifleEnergyCapacity() {
        if(!CONFIG_SPEC.isLoaded()) {
            return plasmaRifleEnergyCapacity.getDefault();
        }
        return plasmaRifleEnergyCapacity.get();
    }

    public int getPlasmaRifleEnergyTransfer() {
        if(!CONFIG_SPEC.isLoaded()) {
            return plasmaRifleEnergyTransfer.getDefault();
        }
        return plasmaRifleEnergyTransfer.get();
    }

    public int getRailgunCooldown() {
        if(!CONFIG_SPEC.isLoaded()) {
            return railgunCooldown.getDefault();
        }
        return railgunCooldown.get();
    }

    public int getRailgunEnergyUsage() {
        if(!CONFIG_SPEC.isLoaded()) {
            return railgunEnergyUsage.getDefault();
        }
        return railgunEnergyUsage.get();
    }

    public int getRailgunReloadTime() {
        if(!CONFIG_SPEC.isLoaded()) {
            return railgunReloadTime.getDefault();
        }
        return railgunReloadTime.get();
    }

    public int getRailgunMaxAmmo() {
        if(!CONFIG_SPEC.isLoaded()) {
            return railgunMaxAmmo.getDefault();
        }
        return railgunMaxAmmo.get();
    }

    public int getRailgunEnergyCapacity() {
        if(!CONFIG_SPEC.isLoaded()) {
            return railgunEnergyCapacity.getDefault();
        }
        return railgunEnergyCapacity.get();
    }

    public int getRailgunEnergyTransfer() {
        if(!CONFIG_SPEC.isLoaded()) {
            return railgunEnergyTransfer.getDefault();
        }
        return railgunEnergyTransfer.get();
    }

    public int getTeslaMinigunCooldown() {
        if(!CONFIG_SPEC.isLoaded()) {
            return teslaMinigunCooldown.getDefault();
        }
        return teslaMinigunCooldown.get();
    }

    public int getTeslaMinigunEnergyUsage() {
        if(!CONFIG_SPEC.isLoaded()) {
            return teslaMinigunEnergyUsage.getDefault();
        }
        return teslaMinigunEnergyUsage.get();
    }

    public int getTeslaMinigunReloadTime() {
        if(!CONFIG_SPEC.isLoaded()) {
            return teslaMinigunReloadTime.getDefault();
        }
        return teslaMinigunReloadTime.get();
    }

    public int getTeslaMinigunHeatPerShot() {
        if(!CONFIG_SPEC.isLoaded()) {
            return teslaMinigunHeatPerShot.getDefault();
        }
        return teslaMinigunHeatPerShot.get();
    }

    public int getTeslaMinigunEnergyCapacity() {
        if(!CONFIG_SPEC.isLoaded()) {
            return teslaMinigunEnergyCapacity.getDefault();
        }
        return teslaMinigunEnergyCapacity.get();
    }

    public int getTeslaMinigunEnergyTransfer() {
        if(!CONFIG_SPEC.isLoaded()) {
            return teslaMinigunEnergyTransfer.getDefault();
        }
        return teslaMinigunEnergyTransfer.get();
    }

    public int getGauntletEnergyUsage() {
        if(!CONFIG_SPEC.isLoaded()) {
            return gauntletEnergyUsage.getDefault();
        }
        return gauntletEnergyUsage.get();
    }

    public int getGauntletEnergyCapacity() {
        if(!CONFIG_SPEC.isLoaded()) {
            return gauntletEnergyCapacity.getDefault();
        }
        return gauntletEnergyCapacity.get();
    }

    public int getGauntletEnergyTransfer() {
        if(!CONFIG_SPEC.isLoaded()) {
            return gauntletEnergyTransfer.getDefault();
        }
        return gauntletEnergyTransfer.get();
    }
}
