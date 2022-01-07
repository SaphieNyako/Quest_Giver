package com.feywild.quest_giver.network;

import com.feywild.quest_giver.network.quest.*;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.network.NetworkX;
import net.minecraftforge.network.NetworkDirection;

public class QuestGiverNetwork extends NetworkX {

    public QuestGiverNetwork(ModX mod) {
        super(mod);
    }

    @Override
    protected Protocol getProtocol() {
        return Protocol.of("1");
    }

    @Override
    protected void registerPackets() {

        this.register(new OpenQuestSelectionSerializer(), () -> OpenQuestSelectionHandler::handle, NetworkDirection.PLAY_TO_CLIENT);
        this.register(new OpenQuestDisplaySerializer(), () -> OpenQuestDisplayHandler::handle, NetworkDirection.PLAY_TO_CLIENT);
        this.register(new SelectQuestSerializer(), () -> SelectQuestHandler::handle, NetworkDirection.PLAY_TO_SERVER);
        this.register(new ConfirmQuestSerializer(), () -> ConfirmQuestHandler::handle, NetworkDirection.PLAY_TO_SERVER);
    }
}
