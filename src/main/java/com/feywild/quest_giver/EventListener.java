package com.feywild.quest_giver;

import com.feywild.quest_giver.entity.GuildMasterProfession;
import com.feywild.quest_giver.network.quest.OpenQuestDisplaySerializer;
import com.feywild.quest_giver.network.quest.OpenQuestSelectionSerializer;
import com.feywild.quest_giver.network.quest.SyncRenders;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.quest.player.QuestLineData;
import com.feywild.quest_giver.quest.task.*;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import com.feywild.quest_giver.util.QuestGiverPlayerData;
import com.feywild.quest_giver.util.RenderEnum;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventListener {

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event){
        QuestGiverPlayerData.copy(event.getOriginal(), event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinWorldEvent event) {
        if(event.getWorld() instanceof ServerLevel) if(event.getEntity() instanceof ServerPlayer player) syncPlayerRenders(player);
    }

    @SuppressWarnings("removal")
    public static void syncPlayerRenders(ServerPlayer player) {
        QuestData data = QuestData.get(player);
        List<String> markedNumbers = new ArrayList<>();
        StringBuilder packet = new StringBuilder();
        for (QuestNumber questNumber : data.getAllQuestLines().keySet()) {
            QuestGiverMod.getInstance().logger.info("number: "+questNumber.id);
            if (data.getQuestLine(questNumber) != null) {
                packet.append(encodeStuff(Objects.requireNonNull(data.getQuestLine(questNumber)))).append("%");
                markedNumbers.add(questNumber.id);
            }
        }
        for(QuestNumber numbers : QuestNumber.values()) {
            if(!markedNumbers.contains(numbers.id)) packet.append(numbers.id).append(",").append(RenderEnum.EXCLAMATION.getId()).append("%");
        }
        QuestGiverMod.getNetwork().sendTo(new SyncRenders(packet.substring(0,packet.length()-1)),player);
    }

    private static String encodeStuff(QuestLineData data) {
        String id = RenderEnum.EXCLAMATION.getId();
        if(data.finished) id = RenderEnum.NONE.getId();
        else if(!data.getActiveQuests().isEmpty()) id = RenderEnum.QUESTION.getId();
        return data.questNumber.id+","+id;
    }

    @SubscribeEvent
    public static void pickupItem(PlayerEvent.ItemPickupEvent event){
        if (event.getPlayer() instanceof  ServerPlayer player){
            for (int i = 0; i < event.getStack().getCount(); i++) {
                QuestData.get(player).checkComplete(ItemPickupTask.INSTANCE, event.getStack());
            }

        }
    }

    @SubscribeEvent
    public static void craftItem(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            QuestData.get((ServerPlayer) event.getPlayer()).checkComplete(CraftTask.INSTANCE, event.getCrafting());
        }
    }

    @SubscribeEvent
    public static void playerKill(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            QuestData quests = QuestData.get(player);
            quests.checkComplete(KillTask.INSTANCE, event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event){

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
    public static void entityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getPlayer();

        if(player instanceof ServerPlayer && event.getTarget() instanceof Villager villager && villager.getVillagerData().getProfession() == GuildMasterProfession.GUILDMASTER.get()) {
            InteractionHand hand = event.getPlayer().getUsedItemHand();
            ItemStack stack = player.getItemInHand(hand);
            if(stack.isEmpty()){
                interactQuest((ServerPlayer) player, hand, villager, QuestNumber.QUEST_0014);
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

    private static void interactQuest(ServerPlayer player, InteractionHand hand, Entity entity, QuestNumber questNumber) {

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
    private static Villager findTarget(Level level, Player player) {
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
