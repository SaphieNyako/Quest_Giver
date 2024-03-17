package com.feywild.quest_giver.events;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.network.quest.SyncPlayerGuiStatus;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(value = Dist.CLIENT)
public class ClientEvents {

    private static boolean showGui = true;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void showGui(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && !showGui) {
            event.setCanceled(true);
        }
    }

    public static void setShowGui(boolean showGui) {
        ClientEvents.showGui = showGui;
        if (Minecraft.getInstance().player != null)
            QuestGiverMod.getNetwork()
                    .sendToServer(new SyncPlayerGuiStatus(
                            Minecraft.getInstance().player.getUUID(), showGui));
    }

    public static boolean getShowGui() {
        return showGui;
    }
}
