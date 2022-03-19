package com.feywild.quest_giver.network.quest;


import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.FriendlyByteBuf;

public class OpenQuestDisplaySerializer implements PacketSerializer<OpenQuestDisplaySerializer.Message> {

    @Override
    public Class<Message> messageClass() {
        return Message.class;
    }

    @Override
    public void encode(Message msg, FriendlyByteBuf buffer) {
        msg.display.toNetwork(buffer);
        buffer.writeBoolean(msg.confirmationButtons);
        buffer.writeEnum(msg.questNumber);
    }

    @Override
    public Message decode(FriendlyByteBuf buffer) {
        QuestDisplay display = QuestDisplay.fromNetwork(buffer);
        boolean confirmationButtons = buffer.readBoolean();
        QuestNumber questNumber = buffer.readEnum(QuestNumber.class);
        return new Message(display, confirmationButtons, questNumber);
    }

    public static class Message {

        public final QuestDisplay display;
        public final boolean confirmationButtons;
        public final QuestNumber questNumber;

        public Message(QuestDisplay display, boolean confirmationButtons, QuestNumber questNumber) {
            this.display = display;
            this.confirmationButtons = confirmationButtons;
            this.questNumber = questNumber;
        }
    }
}
