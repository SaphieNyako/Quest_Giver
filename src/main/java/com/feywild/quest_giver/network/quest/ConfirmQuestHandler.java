package com.feywild.quest_giver.network.quest;


import com.feywild.quest_giver.quest.player.QuestData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ConfirmQuestHandler {

    public static void handle(com.feywild.quest_giver.network.quest.ConfirmQuestSerializer.Message msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                if (msg.accept) {
                    QuestData.get(player).acceptQuestNumber(msg.questNumber);
                } else {
                    QuestData.get(player).acceptQuestNumber(msg.questNumber);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
