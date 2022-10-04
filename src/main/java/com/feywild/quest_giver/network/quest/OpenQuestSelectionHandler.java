package com.feywild.quest_giver.network.quest;


import com.feywild.quest_giver.screen.SelectQuestScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenQuestSelectionHandler {

    public static void handle(OpenQuestSelectionSerializer.Message msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> Minecraft.getInstance().setScreen(new SelectQuestScreen(msg.title, msg.quests, msg.questNumber, msg.pos, msg.id)));
        context.get().setPacketHandled(true);

    }
}
