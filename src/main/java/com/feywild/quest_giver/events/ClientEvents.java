package com.feywild.quest_giver.events;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.item.TradingContract;
import com.feywild.quest_giver.network.quest.SyncPlayerGuiStatus;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.quest.task.CraftTask;
import com.feywild.quest_giver.tag.ModTags;
import com.feywild.quest_giver.util.QuestGiverPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(value = Dist.CLIENT)
public class ClientEvents {

    private static boolean showGui = true;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void showGui(RenderGameOverlayEvent.Pre event){
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL && !getShowGui()){
            event.setCanceled(true);
        }
    }

    public static void setShowGui(boolean showGui) {
      ClientEvents.showGui = showGui;
      if(Minecraft.getInstance().player!=null)
          QuestGiverMod.getNetwork().sendToServer(new SyncPlayerGuiStatus(Minecraft.getInstance().player.getUUID(),showGui));
    }

    public static boolean getShowGui(){
        return showGui;
    }

}
