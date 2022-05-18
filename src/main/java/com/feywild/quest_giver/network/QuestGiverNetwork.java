package com.feywild.quest_giver.network;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.network.quest.*;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.network.NetworkX;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class QuestGiverNetwork extends NetworkX {

    private  static  final  String  PROTOCOL_VERSION  =  "1.0";
    public static final SimpleChannel BETTER_NETWORK = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(QuestGiverMod.getInstance().modid,"better_network"))
            .clientAcceptedVersions ( PROTOCOL_VERSION :: equals)
            .serverAcceptedVersions ( PROTOCOL_VERSION :: equals)
            .networkProtocolVersion (() ->  PROTOCOL_VERSION )
            .simpleChannel ();
    public static int disc = 0;

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
        BETTER_NETWORK.registerMessage(disc++,SyncRenders.class, SyncRenders::encode, SyncRenders::new, SyncRenders::handle);
    }

    public void sendTo(Object message,ServerPlayer player) {
        BETTER_NETWORK.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
