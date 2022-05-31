package com.feywild.quest_giver.network.quest;

import com.feywild.quest_giver.quest.QuestNumber;
import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SelectQuestSerializer implements PacketSerializer<SelectQuestSerializer.Message> {

    @Override
    public Class<Message> messageClass() {
        return Message.class;
    }

    @Override
    public void encode(Message msg, FriendlyByteBuf buffer) {
        buffer.writeComponent(msg.title);
        buffer.writeResourceLocation(msg.quest);
        buffer.writeEnum(msg.questNumber);
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public Message decode(FriendlyByteBuf buffer) {
        Component title = buffer.readComponent();
        ResourceLocation quest = buffer.readResourceLocation();
        QuestNumber questNumber = buffer.readEnum(QuestNumber.class);
        BlockPos pos = buffer.readBlockPos();
        return new Message(quest, title, questNumber, pos);
    }

    public static class Message {
        public final Component title;
        public final ResourceLocation quest;
        public final QuestNumber questNumber;
        public final BlockPos pos;

        public Message(ResourceLocation quest, Component title, QuestNumber questNumber, BlockPos pos) {
            this.quest = quest;
            this.title = title;
            this.questNumber = questNumber;
            this.pos = pos;
        }
    }
}
