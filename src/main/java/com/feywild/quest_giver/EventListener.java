package com.feywild.quest_giver;

import com.feywild.quest_giver.network.quest.OpenQuestDisplaySerializer;
import com.feywild.quest_giver.network.quest.OpenQuestSelectionSerializer;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.quest.task.CraftTask;
import com.feywild.quest_giver.quest.task.KillTask;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class EventListener {

    @SubscribeEvent
    public void craftItem(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            QuestData.get((ServerPlayer) event.getPlayer()).checkComplete(CraftTask.INSTANCE, event.getCrafting());
        }
    }


    @SubscribeEvent
    public void playerKill(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            QuestData quests = QuestData.get(player);
            quests.checkComplete(KillTask.INSTANCE, event.getEntityLiving());
        }
    }


    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event){
        // Only check one / second
        if (event.player.tickCount % 20 == 0 && !event.player.level.isClientSide && event.player instanceof ServerPlayer player) {
            //TODO playerTick check if ItemTask is Completed
            //TODO playerTick for Biome and Structure Check
        }
    }




    @SubscribeEvent
    public void entityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!event.getWorld().isClientSide && event.getPlayer() instanceof ServerPlayer) {
                Player player = event.getPlayer();
                InteractionHand hand = event.getPlayer().getUsedItemHand();

            // TODO IF ENTITY IS VILLAGER || PILLAGER || BLOOD BORN or IF ALLIGNMENT
            if (event.getTarget() instanceof Villager) { //&& event.getTarget().getTags().contains("quest_0001")
                ItemStack stack = player.getItemInHand(hand);
                if (stack.isEmpty()) {
                    this.interactQuest((ServerPlayer) player, hand, event.getTarget(), QuestNumber.QUEST_0001);
                }
            }
        }
    }

    private void interactQuest(ServerPlayer player, InteractionHand hand, Entity entity, QuestNumber questNumber) {

        QuestData quests = QuestData.get(player);

        if (quests.canComplete(questNumber)) {

            QuestDisplay completionDisplay = quests.completePendingQuest();

            if (completionDisplay != null) { //Is there a complete quest pending
                QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                        () -> player), new OpenQuestDisplaySerializer.Message(completionDisplay, false));
                player.swing(hand, true);

            } else {
                List<SelectableQuest> active = quests.getActiveQuests();

                if (active.size() == 1) { //If there is an active quest
                    QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                            () -> player), new OpenQuestDisplaySerializer.Message(active.get(0).display, false));
                    player.swing(hand, true);

                } else if (!active.isEmpty()) {
                    QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                            () -> player), new OpenQuestSelectionSerializer.Message(entity.getDisplayName(), questNumber, active));
                    player.swing(hand, true);
                }
            }
        } else {

            QuestDisplay initDisplay = quests.initialize(questNumber);
            if (initDisplay != null) {
                QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                        () -> player), new OpenQuestDisplaySerializer.Message(initDisplay, true));
                player.swing(hand, true);
            }
        }
    }
}
