package com.feywild.quest_giver;

import com.feywild.quest_giver.entity.GuildMasterProfession;
import com.feywild.quest_giver.network.quest.OpenQuestDisplaySerializer;
import com.feywild.quest_giver.network.quest.OpenQuestSelectionSerializer;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.quest.task.*;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Objects;

public class EventListener {

    @SubscribeEvent
    public void pickupItem(PlayerEvent.ItemPickupEvent event){
        if (event.getPlayer() instanceof  ServerPlayer player){
            for (int i = 0; i < event.getStack().getCount(); i++) {
                QuestData.get(player).checkComplete(ItemPickupTask.INSTANCE, event.getStack());
            }

        }
    }

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
            QuestData quests = QuestData.get(player);
            //Quest Check for ItemStackTask
            player.getInventory().items.forEach(stack -> quests.checkComplete(ItemStackTask.INSTANCE, stack));
            //Quest Check for Biomes
            player.getLevel().getBiome(player.blockPosition()).is(biome -> quests.checkComplete(BiomeTask.INSTANCE, biome.location()));
            //QuestCheck for Structure
             if(player.getLevel().structureFeatureManager().hasAnyStructureAt(player.blockPosition())){
                 player.getLevel().structureFeatureManager().getAllStructuresAt(player.blockPosition()).forEach((structure, set) -> quests.checkComplete(StructureTask.INSTANCE, structure));
             }
        }
    }

    @SubscribeEvent
    public void entityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getPlayer();

        if(player instanceof ServerPlayer && event.getTarget() instanceof Villager villager && villager.getVillagerData().getProfession() == GuildMasterProfession.GUILDMASTER.get()) {
            InteractionHand hand = event.getPlayer().getUsedItemHand();
            ItemStack stack = player.getItemInHand(hand);
            if(stack.isEmpty()){
                this.interactQuest((ServerPlayer) player, hand, villager, QuestNumber.QUEST_0014);
            }
        }
        //TODO add gift item to entity questTask trigger
        /*
        if (!event.getWorld().isClientSide && event.getPlayer() instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer) event.getPlayer();
                InteractionHand hand = event.getPlayer().getUsedItemHand();
                ItemStack stack = player.getItemInHand(hand);
                if (!stack.isEmpty() && QuestData.get(player).checkComplete(GiftTask.INSTANCE, stack)) {
                    if (!player.isCreative()) stack.shrink(1);
                    player.sendMessage(new TranslatableComponent("message.quest_giver.complete"), player.getUUID());
                }
            }
         */
    }

    private void interactQuest(ServerPlayer player, InteractionHand hand, Entity entity, QuestNumber questNumber) {

        QuestData quests = QuestData.get(player);

        if (quests.canComplete(questNumber)) {

            QuestDisplay completionDisplay = Objects.requireNonNull(quests.getQuestLine(questNumber)).completePendingQuest();

            if (completionDisplay != null) {
                QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                        () -> player), new OpenQuestDisplaySerializer.Message(completionDisplay, false, questNumber,  entity.blockPosition()));
                player.swing(hand, true);

            } else {
                List<SelectableQuest> active = Objects.requireNonNull(quests.getQuestLine(questNumber)).getQuests();

                if (active.size() == 1) {
                    QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                            () -> player), new OpenQuestDisplaySerializer.Message(active.get(0).display, false, questNumber,  entity.blockPosition()));
                    player.swing(hand, true);

                } else if (!active.isEmpty()) {
                    QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                            () -> player), new OpenQuestSelectionSerializer.Message(entity.getDisplayName(), questNumber, active, entity.blockPosition()));
                    player.swing(hand, true);
                }
            }
        } else {

            QuestDisplay initDisplay = quests.initialize(questNumber);
            if (initDisplay != null ) {
                QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                        () -> player), new OpenQuestDisplaySerializer.Message(initDisplay, true, questNumber, entity.blockPosition()));
                player.swing(hand, true);
            }
        }
    }

    //This was made to find a Target and give a QuestNumber THIS IS NOT USED RIGHT NOW
    private Villager findTarget(Level level, Player player) {
        double distance = Double.MAX_VALUE;
        TargetingConditions TARGETING = TargetingConditions.forNonCombat().range(8).ignoreLineOfSight();
        Villager current = null;
        for (Villager villager : level.getNearbyEntities(Villager.class, TARGETING, player, player.getBoundingBox().inflate(8))) {
            if (player.distanceToSqr(villager) < distance) {
                current = villager;
                distance = player.distanceToSqr(villager);
            }
        }
        return current;
    }

    //give quest number to villager.
     /*  @SubscribeEvent
        public void playerTick(TickEvent.PlayerTickEvent event) {
            if (event.player.tickCount % 20 == 0 && !event.player.level.isClientSide && event.player instanceof ServerPlayer player) {
            QuestData quests = QuestData.get(player);
            if(quests.getQuestNumbers().size() == 0) {

                if (findTarget(player.level, player) != null) {
                    Villager target = findTarget(player.level, player);
                    target.getTags().add(QuestNumber.QUEST_0001.id);

                    quests.setQuestNumbers(); //This should be done when quest 1 is done.
                }

            } else {

                if (findTarget(player.level, player) != null && !(findTarget(player.level, player).getTags().contains("quest_0001"))){

                    Random random = new Random();
                    Villager target = findTarget(player.level, player);
                    QuestNumber number = quests.getQuestNumbers().get(random.nextInt(quests.getQuestNumbers().size()));
                    //this needs be random...
                    target.getTags().add(QuestNumber.QUEST_0002.id);
                }
            }
        }
     }*/

}
