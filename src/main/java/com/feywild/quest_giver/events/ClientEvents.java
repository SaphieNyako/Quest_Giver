package com.feywild.quest_giver.events;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.network.quest.SyncPlayerGuiStatus;
import com.feywild.quest_giver.tag.ModTags;
import com.feywild.quest_giver.util.QuestGiverPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(value = Dist.CLIENT)
public class ClientEvents {

    private static boolean showGui = true;
    private static BlockPos caveDwellingPos;
    private static BlockPos giantHideoutPos;
    private static BlockPos pillagerBasePos;
    private static BlockPos pillagerHideoutPos;

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

        if (event.player.tickCount % 120 == 0 && !event.player.level.isClientSide && event.player instanceof ServerPlayer player) {
            if((!QuestGiverPlayerData.get(event.player).getBoolean("found_cave_dwelling")) && caveDwellingPos == null) {
                setCaveDwellingPos(player.getLevel().findNearestMapFeature(ModTags.ConfiguredStructureFeatures.CAVE_DWELLING_TAG, player.blockPosition(), 50, true));
                QuestGiverPlayerData.get(event.player).putBoolean("found_cave_dwelling", true);
            }
            if((!QuestGiverPlayerData.get(event.player).getBoolean("found_giant_hideout")) && giantHideoutPos == null) {
                setGiantHideoutPos(player.getLevel().findNearestMapFeature(ModTags.ConfiguredStructureFeatures.GIANT_DWELLING_TAG, player.blockPosition(), 50, true));
                QuestGiverPlayerData.get(event.player).putBoolean("found_giant_hideout", true);
            }
            if((!QuestGiverPlayerData.get(event.player).getBoolean("found_pillager_base")) && pillagerBasePos == null) {
                setPillagerBasePos(player.getLevel().findNearestMapFeature(ModTags.ConfiguredStructureFeatures.PILLAGER_BASE_TAG, player.blockPosition(), 50, true));
                QuestGiverPlayerData.get(event.player).putBoolean("found_pillager_base", true);
            }
            if((!QuestGiverPlayerData.get(event.player).getBoolean("found_pillager_hideout")) && pillagerHideoutPos == null) {
                setPillagerHideoutPos(player.getLevel().findNearestMapFeature(ModTags.ConfiguredStructureFeatures.PILLAGER_HIDEOUT_TAG, player.blockPosition(), 50, true));
                QuestGiverPlayerData.get(event.player).putBoolean("found_pillager_hideout", true);
            }
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


    public static BlockPos getCaveDwellingPos() {
        return caveDwellingPos;
    }

    public static void setCaveDwellingPos(BlockPos structurePos) {
        ClientEvents.caveDwellingPos = structurePos;
    }

    public static BlockPos getGiantHideoutPos(){
        return giantHideoutPos;
    }

    public static void setGiantHideoutPos(BlockPos structurePos){
        ClientEvents.giantHideoutPos = structurePos;
    }

    public static BlockPos getPillagerBasePos(){
        return pillagerBasePos;
    }

    public static void setPillagerBasePos(BlockPos structurePos){
        ClientEvents.pillagerBasePos = structurePos;
    }

    public static BlockPos getPillagerHideoutPos(){
        return pillagerHideoutPos;
    }

    public static void setPillagerHideoutPos(BlockPos structurePos){
        ClientEvents.pillagerBasePos = structurePos;
    }
}
