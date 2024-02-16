package com.feywild.quest_giver.network.quest;

import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class PlaySoundSerializer implements PacketSerializer<PlaySoundSerializer.Message> {

    @Override
    public Class<Message> messageClass() {
        return PlaySoundSerializer.Message.class;
    }

    @Override
    public void encode(Message msg, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(msg.playSound);
    }

    @Override
    public Message decode(FriendlyByteBuf buffer) {
        ResourceLocation playsound = buffer.readResourceLocation();
        return new Message(playsound);
    }

    public static class Message {

        public final ResourceLocation playSound;

        public Message(ResourceLocation playSound) {
            this.playSound = playSound;
        }
    }
}
