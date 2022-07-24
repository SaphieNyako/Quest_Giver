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
import com.feywild.quest_giver.util.QuestGiverPlayerData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class QuestGuardVillager extends Guard {

    public static final EntityDataAccessor<Integer> QUEST_NUMBER = SynchedEntityData.defineId(QuestGuardVillager.class, EntityDataSerializers.INT);
    private boolean setQuestNumber = false;

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
        compound.putBoolean("SetQuestNumber", this.setQuestNumber);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        super.readAdditionalSaveData(compound);
        if(compound.contains("QuestNumber")) {
            this.entityData.set(QUEST_NUMBER, compound.getInt("QuestNumber"));
        }
        this.setQuestNumber = compound.getBoolean("SetQuestNumber");
    }

    @Nonnull
    @Override
    public InteractionResult mobInteract(@Nonnull Player player , @Nonnull InteractionHand hand) {
        boolean foundPillagerHideout = QuestGiverPlayerData.get(player).getBoolean("found_pillager_hideout");
        boolean foundGiantHideout = QuestGiverPlayerData.get(player).getBoolean("found_giant_hideout");
        boolean foundCaveDwelling = QuestGiverPlayerData.get(player).getBoolean("found_cave_dwelling");
        boolean foundPillagerBase = QuestGiverPlayerData.get(player).getBoolean("found_pillager_base");

        if (player instanceof ServerPlayer) {
            if(foundPillagerHideout){
                this.setQuestNumber(24);
            }
            else if (foundPillagerBase){
                this.setQuestNumber(109);
            }
            else if (foundCaveDwelling){
                this.setQuestNumber(110);
            }
            else if (foundGiantHideout){
                this.setQuestNumber(111);
            }

            else {
                this.setQuestNumber(getRandomNumber(108, 108, 23));
            }

            if (this.tryAcceptGift((ServerPlayer) player, hand)) {
                player.swing(hand, true);
            } else {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.isEmpty()) {
                    PlayerPatch<?> playerPatch = (PlayerPatch<?>) player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY, null).orElse(null);
                    if(!playerPatch.isBattleMode()) {
                        this.interactQuest((ServerPlayer) player, hand);
                    } else {
                        ((ServerPlayer) player).sendMessage(new TextComponent("Please be careful not to punch the locals! Please Leave Battle Mode before interacting."), player.getUUID());
                    }
                }
            }
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }

    private void interactQuest(ServerPlayer player, InteractionHand hand) {

        QuestData quests = QuestData.get(player);
        Component name = this.hasCustomName() ? getCustomName() : getDisplayName();

        if(this.getQuestNumber()!=null) { //returns null if the villager has no profession
            if (quests.canComplete(this.getQuestNumber())) {
                QuestDisplay completionDisplay = Objects.requireNonNull(quests.getQuestLine(this.getQuestNumber())).completePendingQuest();

                if (completionDisplay != null) {
                    QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                            () -> player), new OpenQuestDisplaySerializer.Message(completionDisplay, false, name, this.getQuestNumber(), this.blockPosition()));
                    player.swing(hand, true);

                } else {
                    List<SelectableQuest> active = Objects.requireNonNull(quests.getQuestLine(this.getQuestNumber())).getQuests();

                    if (active.size() == 1) {
                        QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                                () -> player), new OpenQuestDisplaySerializer.Message(active.get(0).display, false, name, this.getQuestNumber(), this.blockPosition()));
                        player.swing(hand, true);

                    } else if (!active.isEmpty()) {
                        QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                                () -> player), new OpenQuestSelectionSerializer.Message(name, this.getQuestNumber(), active, this.blockPosition()));
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
                            () -> player), new OpenQuestDisplaySerializer.Message(initDisplay, true, name, this.getQuestNumber(), this.blockPosition()));
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

    public int getRandomNumber(int min, int max, int start){
        setQuestNumber = true;
        Random random = new Random();
        int sum = max - min;
        int randomNumber = max - random.nextInt(sum) ;
        if (random.nextInt(sum + 1) <= 1) {
            return randomNumber;
        }
        else {
            return start;
        }
    }
}
