package com.github.x3r.mekanism_weaponry.client.sound;

import com.github.x3r.mekanism_weaponry.common.item.TeslaMinigunItem;
import com.github.x3r.mekanism_weaponry.common.registry.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class TeslaMinigunSoundInstance extends AbstractTickableSoundInstance {

    private final Player player;

    public TeslaMinigunSoundInstance(Player player) {
        super(SoundRegistry.MINIGUN_SHOOT.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.player = player;
    }

    @Override
    public void tick() {
        if(player == null) {
            this.stop();
            return;
        } else {
            this.x = player.getX();
            this.y = player.getY();
            this.z = player.getZ();
        }
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(stack.getItem() instanceof TeslaMinigunItem item && item.isShooting(stack)) {
            this.volume = 1.0F;
        } else {
            this.volume -= 0.1F;
            if(volume <= 0.1F) {
                this.stop();
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TeslaMinigunSoundInstance) obj;
        return Objects.equals(this.player, that.player);
    }

    public static void playSound(Player player) {
        SoundManager manager = Minecraft.getInstance().getSoundManager();
        TeslaMinigunSoundInstance instance = new TeslaMinigunSoundInstance(player);
        if(!manager.isActive(instance)) {
            manager.play(instance);
        }
    }
}
