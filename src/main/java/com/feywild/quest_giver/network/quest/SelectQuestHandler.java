package com.feywild.quest_giver.network.quest;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.player.QuestData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class SelectQuestHandler {

    public static void handle(SelectQuestSerializer.Message msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                QuestDisplay display = QuestData.get(player).getActiveQuestDisplay(msg.quest);
                if (display != null) {
                    QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(() -> player), new OpenQuestDisplaySerializer.Message(display, false));
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
