package com.feywild.quest_giver.network.quest;


import com.feywild.quest_giver.quest.player.QuestData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ConfirmQuestHandler {

    public static void handle(com.feywild.quest_giver.network.quest.ConfirmQuestSerializer.Message msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                if (msg.accept) {
                    QuestData.get(player).acceptQuestNumber(msg.questNumber);
                    if(QuestData.get(player).getQuestLine(msg.questNumber)!=null) {
                        Objects.requireNonNull(QuestData.get(player).getQuestLine(msg.questNumber)).setRender(msg.icon.getId());
                    }
                } else {
                    QuestData.get(player).denyQuest(msg.questNumber);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
