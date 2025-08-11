package com.github.x3r.mekanism_weaponry;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MekanismWeaponryConfig {

    public static final MekanismWeaponryConfig CONFIG;
    public static final ForgeConfigSpec CONFIG_SPEC;

    private final ForgeConfigSpec.DoubleValue plasmaRifleDamage;
    private final ForgeConfigSpec.IntValue plasmaRifleCooldown;
    private final ForgeConfigSpec.IntValue plasmaRifleEnergyUsage;
    private final ForgeConfigSpec.IntValue plasmaRifleReloadTime;
    private final ForgeConfigSpec.IntValue plasmaRifleHeatPerShot;
    private final ForgeConfigSpec.IntValue plasmaRifleEnergyCapacity;
    private final ForgeConfigSpec.IntValue plasmaRifleEnergyTransfer;

    private final ForgeConfigSpec.DoubleValue railgunDamage;
    private final ForgeConfigSpec.IntValue railgunCooldown;
    private final ForgeConfigSpec.IntValue railgunEnergyUsage;
    private final ForgeConfigSpec.IntValue railgunReloadTime;
    private final ForgeConfigSpec.IntValue railgunMaxAmmo;
    private final ForgeConfigSpec.IntValue railgunEnergyCapacity;
    private final ForgeConfigSpec.IntValue railgunEnergyTransfer;
    private final ForgeConfigSpec.DoubleValue railgunSecondModeScale;

    private final ForgeConfigSpec.DoubleValue teslaMinigunDamage;
    private final ForgeConfigSpec.IntValue teslaMinigunCooldown;
    private final ForgeConfigSpec.IntValue teslaMinigunEnergyUsage;
    private final ForgeConfigSpec.IntValue teslaMinigunReloadTime;
    private final ForgeConfigSpec.IntValue teslaMinigunHeatPerShot;
    private final ForgeConfigSpec.IntValue teslaMinigunEnergyCapacity;
    private final ForgeConfigSpec.IntValue teslaMinigunEnergyTransfer;

    private final ForgeConfigSpec.DoubleValue gauntletDamage;
    private final ForgeConfigSpec.IntValue gauntletEnergyUsage;
    private final ForgeConfigSpec.IntValue gauntletEnergyCapacity;
    private final ForgeConfigSpec.IntValue gauntletEnergyTransfer;

    static {
        Pair<MekanismWeaponryConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(MekanismWeaponryConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    public MekanismWeaponryConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Mekanism Weaponry Config");
        
        plasmaRifleDamage = builder.comment("Damage dealt by plasma rifle projectile").defineInRange("plasmaRifleDamage", 8.0, 0, Float.MAX_VALUE);
        plasmaRifleCooldown = builder.comment("Cooldown in ticks between when gun can be shot").defineInRange("plasmaRifleCooldown", 6, 0, Integer.MAX_VALUE);
        plasmaRifleEnergyUsage = builder.comment("How much FE should be drained from the guns battery every shot").defineInRange("plasmaRifleEnergyUsage", 175, 0, Integer.MAX_VALUE);
        plasmaRifleReloadTime = builder.comment("How many ticks it should take to reload the weapon").defineInRange("plasmaRifleReloadTime", 45, 0, Integer.MAX_VALUE);
        plasmaRifleHeatPerShot = builder.comment("How much heat should accumulate every shot").defineInRange("plasmaRifleHeatPerShot", 20, 0, Integer.MAX_VALUE);
        plasmaRifleEnergyCapacity = builder.comment("How much energy the gun can store").defineInRange("plasmaRifleEnergyCapacity", 50000, 0, Integer.MAX_VALUE);
        plasmaRifleEnergyTransfer = builder.comment("How fast the gun battery can charge/discharge").defineInRange("plasmaRifleEnergyTransfer", 1000, 0, Integer.MAX_VALUE);

        railgunDamage = builder.comment("Damage dealt by railgun rod").defineInRange("railgunDamage", 16.0, 0, Float.MAX_VALUE);
        railgunCooldown = builder.comment("Cooldown in ticks between when gun can be shot").defineInRange("railgunCooldown", 20, 0, Integer.MAX_VALUE);
        railgunEnergyUsage = builder.comment("How much FE should be drained from the guns battery every shot").defineInRange("railgunEnergyUsage", 1000, 0, Integer.MAX_VALUE);
        railgunReloadTime = builder.comment("How many ticks it should take to reload the weapon").defineInRange("railgunReloadTime", 55, 0, Integer.MAX_VALUE);
        railgunMaxAmmo = builder.comment("Max ammo the gun can hold").defineInRange("railgunMaxAmmo", 5, 1, Integer.MAX_VALUE);
        railgunEnergyCapacity = builder.comment("How much energy the gun can store").defineInRange("railgunEnergyCapacity", 75000, 0, Integer.MAX_VALUE);
        railgunEnergyTransfer = builder.comment("How fast the gun battery can charge/discharge").defineInRange("railgunEnergyTransfer", 10000, 0, Integer.MAX_VALUE);
        railgunSecondModeScale = builder.comment("Stats scaling when railgun is in second mode").defineInRange("railgunSecondModeScale", 2, 0, Float.MAX_VALUE);

        teslaMinigunDamage = builder.comment("Damage dealt by tesla minigun").defineInRange("teslaMinigunDamage", 2.5, 0, Float.MAX_VALUE);
        teslaMinigunCooldown = builder.comment("Cooldown in ticks between when gun can be shot").defineInRange("teslaMinigunCooldown", 4, 0, Integer.MAX_VALUE);
        teslaMinigunEnergyUsage = builder.comment("How much FE should be drained from the guns battery every shot").defineInRange("teslaMinigunEnergyUsage", 500, 0, Integer.MAX_VALUE);
        teslaMinigunReloadTime = builder.comment("How many ticks it should take to reload the weapon").defineInRange("teslaMinigunReloadTime", 100, 0, Integer.MAX_VALUE);
        teslaMinigunHeatPerShot = builder.comment("How much heat should accumulate every shot").defineInRange("teslaMinigunHeatPerShot", 5, 0, Integer.MAX_VALUE);
        teslaMinigunEnergyCapacity = builder.comment("How much energy the gun can store").defineInRange("teslaMinigunEnergyCapacity", 75000, 0, Integer.MAX_VALUE);
        teslaMinigunEnergyTransfer = builder.comment("How fast the gun battery can charge/discharge").defineInRange("teslaMinigunEnergyTransfer", 10000, 0, Integer.MAX_VALUE);

        gauntletDamage = builder.comment("Damage dealt by gauntlet").defineInRange("gauntletDamage", 5, 0, Float.MAX_VALUE);
        gauntletEnergyUsage = builder.comment("How much FE should be drained from the battery every shot").defineInRange("gauntletEnergyUsage", 1000, 0, Integer.MAX_VALUE);
        gauntletEnergyCapacity = builder.comment("How much energy the gauntlet battery can store").defineInRange("gauntletEnergyCapacity", 15000, 0, Integer.MAX_VALUE);
        gauntletEnergyTransfer = builder.comment("How fast the gauntlet battery can charge/discharge").defineInRange("gauntletEnergyTransfer", 1000, 0, Integer.MAX_VALUE);

        builder.pop();
    }

    public double getPlasmaRifleDamage() {
        if(!CONFIG_SPEC.isLoaded()) {
            return plasmaRifleDamage.get();
        }
        return plasmaRifleDamage.getDefault();
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

    public double getRailgunDamage() {
        if(!CONFIG_SPEC.isLoaded()) {
            return railgunDamage.get();
        }
        return railgunDamage.getDefault();
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

    public double getRailgunSecondModeScale() {
        if(!CONFIG_SPEC.isLoaded()) {
            return railgunSecondModeScale.get();
        }
        return railgunSecondModeScale.getDefault();
    }

    public double getTeslaMinigunDamage() {
        if(!CONFIG_SPEC.isLoaded()) {
            return teslaMinigunDamage.get();
        }
        return teslaMinigunDamage.getDefault();
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

    public double getGauntletDamage() {
        if(!CONFIG_SPEC.isLoaded()) {
            return gauntletDamage.get();
        }
        return gauntletDamage.getDefault();
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
