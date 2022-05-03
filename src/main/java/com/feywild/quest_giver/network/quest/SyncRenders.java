package com.feywild.quest_giver.network.quest;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.events.RenderEvents;
import com.feywild.quest_giver.util.RenderEnum;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.function.Supplier;

public class SyncRenders {
    private final String s;

    public SyncRenders(FriendlyByteBuf buf) {
        this.s = ((String) buf.readCharSequence(buf.readableBytes(), StandardCharsets.UTF_8));
    }

    public SyncRenders(String renders) {
        this.s = renders;
        QuestGiverMod.getInstance().logger.info(s);
    }

    public static void encode(SyncRenders packet, FriendlyByteBuf buf) {
        buf.writeCharSequence(packet.s, StandardCharsets.UTF_8);
    }

    public static void handle(final SyncRenders packet, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {});
        RenderEvents.renders = packet.getRenders();
        ctx.setPacketHandled(true);
    }

    public HashMap<String,RenderEnum> getRenders() {
        if(s==null || s.isBlank() || s.isEmpty()) return new HashMap<>();
        HashMap<String,RenderEnum> builtMap = new HashMap<>();
        String[] pairs = stringBreaker(s,"%");
        for(String pair : pairs) {
            String[] broken = stringBreaker(pair,",");
            builtMap.put(broken[0],RenderEnum.getRender(broken[1]));
        }
        return builtMap;
    }

    public static String[] stringBreaker(String s, String regex) {
        return s.split(regex);
    }
}
