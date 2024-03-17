package com.feywild.quest_giver.network.quest;

import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.util.RenderEnum;
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
        buffer.writeEnum(msg.icon);
    }

    @Override
    public Message decode(FriendlyByteBuf buffer) {
        boolean accept = buffer.readBoolean();
        QuestNumber questNumber = buffer.readEnum(QuestNumber.class);
        RenderEnum icon = buffer.readEnum(RenderEnum.class);
        return new Message(accept, questNumber, icon);
    }

    public record Message(boolean accept, QuestNumber questNumber, RenderEnum icon) {}
}
