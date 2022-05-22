package com.feywild.quest_giver.entity;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.events.ClientEvents;
import com.feywild.quest_giver.network.quest.OpenQuestDisplaySerializer;
import com.feywild.quest_giver.network.quest.OpenQuestSelectionSerializer;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.quest.task.GiftTask;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import tallestegg.guardvillagers.entities.Guard;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class QuestGuardVillager extends Guard {

    public static final EntityDataAccessor<Integer> QUEST_NUMBER = SynchedEntityData.defineId(QuestGuardVillager.class, EntityDataSerializers.INT);
    private UUID questTaker;

    public QuestGuardVillager(EntityType<? extends Guard> type, Level world) {
        super(type, world);
        this.noCulling = true;
    }

    public static boolean canSpawn(EntityType<? extends QuestGuardVillager> entity, LevelAccessor level, MobSpawnType reason, BlockPos pos, Random random) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(BlockTags.DIRT).contains(level.getBlockState(pos.below()).getBlock()) || Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(BlockTags.SAND).contains(level.getBlockState(pos.below()).getBlock()) ;
    }

    public QuestNumber getQuestNumber(){
            try {
                return QuestNumber.values()[this.entityData.get(QUEST_NUMBER)];
            } catch(ArrayIndexOutOfBoundsException exception) {
                return QuestNumber.values()[0];
            }
    }

    public void setQuestNumber(Integer questNumber){
        this.entityData.set(QUEST_NUMBER, questNumber);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(QUEST_NUMBER, QUEST_NUMBER.getId());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("QuestNumber", this.entityData.get(QUEST_NUMBER));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        super.readAdditionalSaveData(compound);
        if(compound.contains("QuestNumber")) {
            this.entityData.set(QUEST_NUMBER, compound.getInt("QuestNumber"));
        }
    }

    @Nonnull
    @Override
    public InteractionResult mobInteract(@Nonnull Player player , @Nonnull InteractionHand hand) {
        if (player instanceof ServerPlayer) {
            if(ClientEvents.getStructurePos() != null){
                this.setQuestNumber(24);
            }

            if (this.tryAcceptGift((ServerPlayer) player, hand)) {
                player.swing(hand, true);
            } else {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.isEmpty()) {
                    this.interactQuest((ServerPlayer) player, hand);
                }
            }
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }

    private void interactQuest(ServerPlayer player, InteractionHand hand) {

        QuestData quests = QuestData.get(player);

        if(this.getQuestNumber()!=null) { //returns null if the villager has no profession
            if (quests.canComplete(this.getQuestNumber())) {
                QuestDisplay completionDisplay = Objects.requireNonNull(quests.getQuestLine(this.getQuestNumber())).completePendingQuest();

                if (completionDisplay != null) {
                    QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                            () -> player), new OpenQuestDisplaySerializer.Message(completionDisplay, false, this.getQuestNumber(), this.blockPosition()));
                    player.swing(hand, true);

                } else {
                    List<SelectableQuest> active = Objects.requireNonNull(quests.getQuestLine(this.getQuestNumber())).getQuests();

                    if (active.size() == 1) {
                        QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                                () -> player), new OpenQuestDisplaySerializer.Message(active.get(0).display, false, this.getQuestNumber(), this.blockPosition()));
                        player.swing(hand, true);

                    } else if (!active.isEmpty()) {
                        QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                                () -> player), new OpenQuestSelectionSerializer.Message(this.getDisplayName(), this.getQuestNumber(), active, this.blockPosition()));
                        player.swing(hand, true);
                    } else {
                        if (!this.level.isClientSide()) {
                            this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
                        }
                    }
                }
            } else {
                QuestDisplay initDisplay = quests.initialize(this.getQuestNumber());
                if (initDisplay != null) {
                    QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                            () -> player), new OpenQuestDisplaySerializer.Message(initDisplay, true, this.getQuestNumber(), this.blockPosition()));
                    player.swing(hand, true);
                } else {
                    if (!this.level.isClientSide()) {
                        this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
                    }
                }
            }
        } else {
            if (!this.level.isClientSide()) {
                this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
            }
        }
    }

    private boolean tryAcceptGift(ServerPlayer player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty()) {
            if (QuestData.get(player).checkComplete(GiftTask.INSTANCE, stack)) {
                if (!player.isCreative()) stack.shrink(1);
                player.sendMessage(new TextComponent("Thank you, "+ player.getName().getContents() + "!"), player.getUUID());
                return true;
            }
        }
        return false;
    }
}
