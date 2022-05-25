package com.feywild.quest_giver.events;

import com.feywild.quest_giver.entity.GuildMasterProfession;
import com.feywild.quest_giver.entity.QuestVillager;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.quest.task.EndTask;
import com.feywild.quest_giver.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(value = Dist.CLIENT)
public class ClientEvents {

    private static boolean showGui = true;
    private static BlockPos structurePos;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void showGui(RenderGameOverlayEvent.Pre event){
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL && !getShowGui()){
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void playerTick(TickEvent.PlayerTickEvent event) {

        if (event.player.tickCount % 120 == 0 && !event.player.level.isClientSide && event.player instanceof ServerPlayer player && structurePos == null) {
            setStructurePos( player.getLevel().findNearestMapFeature(ModTags.ConfiguredStructureFeatures.QUEST_STRUCTURE_TAG, player.blockPosition(), 50, true));
           //keytag structure, pps, distance, below ground
        }
    }


    public static void setShowGui(boolean showGui) {
      ClientEvents.showGui = showGui;
    }

    public static boolean getShowGui(){
        return showGui;
    }


    public static BlockPos getStructurePos() {
        return structurePos;
    }

    public static void setStructurePos(BlockPos structurePos) {
        ClientEvents.structurePos = structurePos;
    }
}
