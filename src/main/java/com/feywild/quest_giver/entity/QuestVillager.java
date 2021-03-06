package com.feywild.quest_giver.entity;

import com.feywild.quest_giver.EventListener;
import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.entity.goals.QuestVillagerInteractGoal;
import com.feywild.quest_giver.item.TradingContract;
import com.feywild.quest_giver.network.quest.OpenQuestDisplaySerializer;
import com.feywild.quest_giver.network.quest.OpenQuestSelectionSerializer;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.quest.task.GiftTask;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import com.samebutdifferent.morevillagers.init.ModProfessions;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class QuestVillager extends Villager {

    public static final EntityDataAccessor<Integer> QUEST_NUMBER = SynchedEntityData.defineId(QuestVillager.class, EntityDataSerializers.INT);
    private boolean setQuestNumber = false;
    private UUID questTaker;

    public QuestVillager(EntityType<? extends Villager> villager, Level level) {
        super(villager, level);
        this.noCulling = true;
    }


    public static boolean canSpawn(EntityType<? extends QuestVillager> entity, LevelAccessor level, MobSpawnType reason, BlockPos pos, Random random) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(BlockTags.DIRT).contains(level.getBlockState(pos.below()).getBlock()) || Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(BlockTags.SAND).contains(level.getBlockState(pos.below()).getBlock()) ;
    }

    public UUID getQuestTaker() {
        return questTaker;
    }

    public void setQuestTaker(Player player) {
        this.questTaker = player.getUUID();
    }

    public Player getPlayer() {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(this.questTaker);
    }

    @Override
    public void tick() {
        super.tick();
        VillagerProfession villagerprofession = this.getVillagerData().getProfession();
        if(villagerprofession != VillagerProfession.NONE && !setQuestNumber) {
            if (villagerprofession == VillagerProfession.ARMORER) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(26,31, 1));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.BUTCHER) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(32, 37, 2));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.CARTOGRAPHER) {
               this.entityData.set(QUEST_NUMBER, getRandomNumber(38,43, 3));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.CLERIC) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(44,50, 4));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.FARMER) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(51,55, 5));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.FISHERMAN) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(56,60, 6));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.FLETCHER) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(61,65, 7));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.LEATHERWORKER) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(66,69, 8));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.LIBRARIAN) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(70,74, 9));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.MASON) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(75,78, 10));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.SHEPHERD) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(79, 81, 11));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.TOOLSMITH) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(82,85, 12));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.WEAPONSMITH) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(86,89, 13));
                this.setVillagerXp(1);
            }

            //GUILDMASTER
            if (villagerprofession == GuildMasterProfession.GUILDMASTER.get()) {
                this.entityData.set(QUEST_NUMBER, 14);
                this.setVillagerXp(1);
            }

            //MORE VILLAGER PROFESSIONS
            if (villagerprofession == ModProfessions.ENDERIAN.get()) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(91,94, 15));
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.ENGINEER.get()) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(95,98, 16));
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.FLORIST.get()) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(99,102, 17));
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.HUNTER.get()) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(103,104, 18));
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.MINER.get()) {
                this.entityData.set(QUEST_NUMBER, 19);
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.NETHERIAN.get()) {
                this.entityData.set(QUEST_NUMBER, 20);
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.OCEANOGRAPHER.get()) {
                this.entityData.set(QUEST_NUMBER, 21);
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.WOODWORKER.get()) {
                this.entityData.set(QUEST_NUMBER, getRandomNumber(105, 107, 22));
                this.setVillagerXp(1);
            }
            if (villagerprofession == com.lupicus.bk.entity.ModProfessions.BEEKEEPER) {
                this.entityData.set(QUEST_NUMBER, 25);
                this.setVillagerXp(1);
            }

        }
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

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.5D).add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    public QuestNumber getQuestNumber(){
        if (this.getVillagerData().getProfession() != VillagerProfession.NONE)
            try {
                return QuestNumber.values()[this.entityData.get(QUEST_NUMBER)];
            } catch(ArrayIndexOutOfBoundsException exception) {
                return QuestNumber.values()[0];
            }
        else return null;
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
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("QuestNumber", this.entityData.get(QUEST_NUMBER));
        if(this.questTaker != null) {
            compound.putUUID("QuestTaker", this.questTaker);
        } else {
          //  serializeNBT().remove("QuestTaker");
        }
        compound.putBoolean("SetQuestNumber", this.setQuestNumber);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if(compound.contains("QuestNumber")) {
            this.entityData.set(QUEST_NUMBER, compound.getInt("QuestNumber"));
        }
        if(compound.contains("QuestTaker")){
            this.questTaker = serializeNBT().hasUUID("QuestTaker") ? compound.getUUID("QuestTaker") : null;
        }
        this.setQuestNumber = compound.getBoolean("SetQuestNumber");
    }

    @Nonnull
    @Override
    public InteractionResult  mobInteract(@Nonnull Player player ,@Nonnull InteractionHand hand) {
        if (player instanceof ServerPlayer) {
            if (this.tryAcceptGift((ServerPlayer) player, hand)) {
                player.swing(hand, true);
            } else {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.isEmpty()) {
                    PlayerPatch<?> playerPatch = (PlayerPatch<?>) player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY, null).orElse(null);
                    if(!playerPatch.isBattleMode()) {
                        this.interactQuest((ServerPlayer) player, hand);
                        this.setTradingPlayer(player);
                    } else {
                        ((ServerPlayer) player).sendMessage(new TextComponent("Please be careful not to punch the locals! Please Leave Battle Mode before interacting."), player.getUUID());
                    }
                }
                if (stack.getItem() instanceof TradingContract contract && Objects.equals(contract.getProfession(), this.getVillagerData().getProfession().getName())
                && contract.playerSignature().equals(player.getName().getString())) {
                   super.mobInteract(player, hand);
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
                if (getQuestTaker() == null) {
                    this.setQuestTaker(player);
                }

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
                        this.setUnhappyCounter(40);
                        if (!this.level.isClientSide()) {
                            this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
                        }
                    }
                }
            } else {
                QuestDisplay initDisplay = quests.initialize(this.getQuestNumber());
                if (initDisplay != null && getQuestTaker() == null) {
                    QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                            () -> player), new OpenQuestDisplaySerializer.Message(initDisplay, true, name, this.getQuestNumber(), this.blockPosition()));
                    player.swing(hand, true);
                } else {
                    this.setUnhappyCounter(40);
                    if (!this.level.isClientSide()) {
                        this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
                    }
                }
            }
        } else {
            this.setUnhappyCounter(40);
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

    @Override
    protected boolean canRide(@NotNull Entity entity) {
        return false;
    }
}
