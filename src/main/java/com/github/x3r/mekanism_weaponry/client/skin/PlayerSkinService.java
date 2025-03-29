package com.github.x3r.mekanism_weaponry.client.skin;

import com.github.x3r.mekanism_weaponry.MekanismWeaponry;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.ProfileResult;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class PlayerSkinService {

    public static Map<UUID, GameProfile> CACHED_PROFILES = new HashMap<>();
    private static MinecraftSessionService sessionService = Minecraft.getInstance().minecraftSessionService;

    public static void reloadCachedSkins() {
        UUID uuid = Minecraft.getInstance().getUser().getProfileId();
        new Thread() {
            @Override
            public void run() {
                try {
                    ProfileResult result = sessionService.fetchProfile(uuid, false);
                    if(result != null) {
                        CACHED_PROFILES.put(uuid, result.profile());
                    }
                } catch (IllegalArgumentException e) {
                    MekanismWeaponry.LOGGER.error("\"{}\" is not a valid UUID!", uuid);
                }

                super.run();
            }
        }.start();
    }
}
