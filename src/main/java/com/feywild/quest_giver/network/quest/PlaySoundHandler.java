package com.feywild.quest_giver.network.quest;

import com.feywild.quest_giver.screen.DisplayQuestScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public class PlaySoundHandler {

    public static void handle(PlaySoundSerializer.Message msg, Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> {
                Player player = Minecraft.getInstance().player;
            @Nullable SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(msg.playSound.getPath()));
                if (player != null && sound != null) {
                    Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(sound, SoundSource.MASTER, 1, 1, player.getX(), player.getY(), player.getZ()));
            }
        });
        context.get().setPacketHandled(true);
    }
}