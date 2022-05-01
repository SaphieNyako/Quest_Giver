package com.feywild.quest_giver.util;

import com.feywild.quest_giver.EventListener;
import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.entity.QuestVillager;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.renderer.ExclamationMarkerRenderer;
import com.sun.jna.platform.win32.WinNT;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


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
    }

    public static boolean getShowGui(){
        return showGui;
    }
}
