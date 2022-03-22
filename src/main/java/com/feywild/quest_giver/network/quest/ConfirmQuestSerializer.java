package com.feywild.quest_giver.network.quest;

import com.feywild.quest_giver.quest.QuestNumber;
import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.FriendlyByteBuf;

public class ConfirmQuestSerializer implements PacketSerializer<ConfirmQuestSerializer.Message> {

    @Override
    public Class<Message> messageClass() {
        return Message.class;
    }

    @Override
    public void encode(Message msg, FriendlyByteBuf buffer) {

        buffer.writeBoolean(msg.accept);
        buffer.writeEnum(msg.questNumber);
    }

    @Override
    public Message decode(FriendlyByteBuf buffer) {

        boolean accept = buffer.readBoolean();
        QuestNumber questNumber = buffer.readEnum(QuestNumber.class);
        return new Message(accept, questNumber);

    }

    public static class Message {

        public final boolean accept;
        public final QuestNumber questNumber;

        public Message(boolean accept, QuestNumber questNumber) {
            this.accept = accept;
            this.questNumber = questNumber;
        }



    }
}
