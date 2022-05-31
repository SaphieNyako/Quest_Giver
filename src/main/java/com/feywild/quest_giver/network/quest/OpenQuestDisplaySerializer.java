package com.feywild.quest_giver.network.quest;


import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class OpenQuestDisplaySerializer implements PacketSerializer<OpenQuestDisplaySerializer.Message> {

    @Override
    public Class<Message> messageClass() {
        return Message.class;
    }

    @Override
    public void encode(Message msg, FriendlyByteBuf buffer) {
        buffer.writeComponent(msg.title);
        msg.display.toNetwork(buffer);
        buffer.writeBlockPos(msg.pos);
        buffer.writeBoolean(msg.confirmationButtons);
        buffer.writeEnum(msg.questNumber);

    }

    @Override
    public Message decode(FriendlyByteBuf buffer) {
        Component title = buffer.readComponent();
        QuestDisplay display = QuestDisplay.fromNetwork(buffer);
        BlockPos pos = buffer.readBlockPos();
        boolean confirmationButtons = buffer.readBoolean();
        QuestNumber questNumber = buffer.readEnum(QuestNumber.class);;

        return new Message(display, confirmationButtons, title, questNumber, pos);
    }

    public static class Message {

        public final Component title;
        public final QuestDisplay display;
        public final boolean confirmationButtons;
        public final QuestNumber questNumber;
        public final BlockPos pos;


        public Message(QuestDisplay display, boolean confirmationButtons, Component title, QuestNumber questNumber, BlockPos pos) {
            this.display = display;
            this.confirmationButtons = confirmationButtons;
            this.title = title;
            this.questNumber = questNumber;
            this.pos = pos;
        }
    }
}
