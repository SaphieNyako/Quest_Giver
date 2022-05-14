package com.feywild.quest_giver;

import com.feywild.quest_giver.entity.GuildMasterProfession;
import com.feywild.quest_giver.entity.ModEntityTypes;
import com.feywild.quest_giver.entity.QuestVillager;
import com.feywild.quest_giver.item.ModItems;
import com.feywild.quest_giver.item.TradingContract;
import com.feywild.quest_giver.network.quest.OpenQuestDisplaySerializer;
import com.feywild.quest_giver.network.quest.OpenQuestSelectionSerializer;
import com.feywild.quest_giver.network.quest.SyncRenders;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.quest.player.QuestLineData;
import com.feywild.quest_giver.quest.task.*;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import com.feywild.quest_giver.tag.ModTags;
import com.feywild.quest_giver.util.RenderEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.data.worldgen.StructureSets;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventListener {

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
        if(data.checkForEnd()) {
            id = RenderEnum.NONE.getId();
        }
        else if(!data.getActiveQuests().isEmpty()) {
            id = RenderEnum.QUESTION.getId();
        }
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
        Player player = event.getPlayer();
        ItemStack stack = event.getCrafting();

        if (player instanceof ServerPlayer) {
            QuestData.get((ServerPlayer) player).checkComplete(CraftTask.INSTANCE, stack);


            if(stack.getItem() instanceof  TradingContract contract && contract.assignedToPlayer == player.getUUID()) {
                contract.signedByPlayer(player);
            } else {
                ((ServerPlayer) player).sendMessage(new TextComponent("You can't sign this contract"), player.getUUID());
                event.setCanceled(true);
            }
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

            for (ItemStack item : player.getInventory().items) {
                if (item.getItem() instanceof TradingContract contract && contract.assignedToPlayer == null) {
                    contract.assignedToPlayer(player);
                }
            }

            //Quest Check for Biomes
            player.getLevel().getBiome(player.blockPosition()).is(biome -> quests.checkComplete(BiomeTask.INSTANCE, biome.location()));
            //QuestCheck for Structure
             if(player.getLevel().structureFeatureManager().hasAnyStructureAt(player.blockPosition())){
                 player.getLevel().structureFeatureManager().getAllStructuresAt(player.blockPosition()).forEach((structure, set) -> quests.checkComplete(StructureTask.INSTANCE, structure));
             }

             //TODO STRUCTURE SEARCH
          //   player.getLevel().findNearestMapFeature(ModTags.ConfiguredStructureFeatures.QUEST_STRUCTURE_TAG, player.blockPosition(), 20, false); //keytag structure, pps, distance, below ground



        }
    }

    @SubscribeEvent
    public static void entityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getPlayer();
        if(player instanceof ServerPlayer) {
            if(event.getTarget() instanceof QuestVillager questvillager) {
                QuestData.get((ServerPlayer) player).checkComplete(EndTask.INSTANCE, questvillager);
            }
            if (event.getTarget() instanceof Villager villager && villager.getVillagerData().getProfession() == GuildMasterProfession.GUILDMASTER.get()) {

                BlockPos spawnPos = villager.blockPosition();
                villager.remove(Entity.RemovalReason.DISCARDED);

                QuestVillager entity = new QuestVillager(ModEntityTypes.questVillager, player.level);
                VillagerData villagerData = new VillagerData(VillagerType.byBiome(player.level.getBiome(player.blockPosition())), GuildMasterProfession.GUILDMASTER.get(), 1);
                entity.setVillagerData(villagerData);
                entity.setVillagerXp(1);
                entity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                entity.setQuestNumber(14);
                player.level.addFreshEntity(entity);


                InteractionHand hand = event.getPlayer().getUsedItemHand();
                ItemStack stack = player.getItemInHand(hand);
                if (stack.isEmpty()) {
                    interactQuest((ServerPlayer) player, hand, entity, QuestNumber.QUEST_0014);
                }
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
