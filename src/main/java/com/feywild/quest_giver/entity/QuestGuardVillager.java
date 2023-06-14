package com.feywild.quest_giver.entity;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.config.QuestConfig;
import com.feywild.quest_giver.entity.goals.QuestGuardInteractWithPlayerGoal;
import com.feywild.quest_giver.network.quest.OpenQuestDisplaySerializer;
import com.feywild.quest_giver.network.quest.OpenQuestSelectionSerializer;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.quest.task.GiftTask;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import mods.thecomputerizer.reputation.api.ReputationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import tallestegg.guardvillagers.configuration.GuardConfig;
import tallestegg.guardvillagers.entities.Guard;
import tallestegg.guardvillagers.entities.ai.goals.*;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings({"rawtypes", "unchecked"})
public class QuestGuardVillager extends Guard {

    public static final EntityDataAccessor<Integer> QUEST_NUMBER = SynchedEntityData.defineId(QuestGuardVillager.class, EntityDataSerializers.INT);
    private boolean setQuestNumber = false;

    private boolean isInteracting = false;
    private Player recentInteractingPlayer = null;

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
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new KickGoal(this));
        this.goalSelector.addGoal(0, new GuardEatFoodGoal(this));
        this.goalSelector.addGoal(0, new RaiseShieldGoal(this));
        this.goalSelector.addGoal(1, new QuestGuardInteractWithPlayerGoal(this));
        this.goalSelector.addGoal(2, new GuardRunToEatGoal(this));
        this.goalSelector.addGoal(2, new GuardSetRunningToEatGoal(this, 1.0));
        this.goalSelector.addGoal(3, new RangedCrossbowAttackPassiveGoal(this, 1.0, 8.0F));
        this.goalSelector.addGoal(3, new RangedBowAttackPassiveGoal(this, 0.5, 20, 15.0F));
        this.goalSelector.addGoal(3, new GuardMeleeGoal(this, 0.8, true));
        this.goalSelector.addGoal(4, new FollowHeroGoal(this));
        if (GuardConfig.GuardsRunFromPolarBears) {
            this.goalSelector.addGoal(4, new AvoidEntityGoal(this, PolarBear.class, 12.0F, 1.0, 1.2));
        }

        this.goalSelector.addGoal(4, new MoveBackToVillageGoal(this, 0.5, false));
        this.goalSelector.addGoal(4, new GolemRandomStrollInVillageGoal(this, 0.5));
        this.goalSelector.addGoal(4, new MoveThroughVillageGoal(this, 0.5, false, 4, () -> false));
        if (GuardConfig.GuardsOpenDoors) {
            this.goalSelector.addGoal(4, new OpenDoorGoal(this, true) {
                public void start() {
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    super.start();
                }
            });
        }

        if (GuardConfig.GuardFormation) {
            this.goalSelector.addGoal(6, new FollowShieldGuards(this));
        }

        if (GuardConfig.ClericHealing) {
            this.goalSelector.addGoal(7, new RunToClericGoal(this));
        }

        if (GuardConfig.armorerRepairGuardArmor) {
            this.goalSelector.addGoal(7, new ArmorerRepairGuardArmorGoal(this));
        }

        this.goalSelector.addGoal(5, new WalkBackToCheckPointGoal(this, 0.5));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, AbstractVillager.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(5, new DefendVillageGuardGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Ravager.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, Guard.class, IronGolem.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Witch.class, true));
        this.targetSelector.addGoal(3, new HeroHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HeroHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, AbstractIllager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Raider.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Illusioner.class, true));
        if (GuardConfig.AttackAllMobs) {
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 5, true, true, (mob) -> mob instanceof Enemy && !GuardConfig.MobBlackList.contains(((Mob)mob).getEncodeId())));
        }

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Zombie.class, true));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal(this, false));
    }

    public boolean isAngryAt(Object entity) {
        if(entity instanceof LivingEntity living) return super.isAngryAt(living);
        return false;
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

    public void setInteracting(boolean interacting) {
        this.isInteracting = interacting;
    }

    public boolean isInteracting() {
        return this.isInteracting;
    }

    public Player getInteractingPlayer() {
        return this.recentInteractingPlayer;
    }

    public void setInteractingPlayer(Player player) {
        this.recentInteractingPlayer = player;
    }

    @Nonnull
    @Override
    public InteractionResult mobInteract(@Nonnull Player player , @Nonnull InteractionHand hand) {
    	if (player.isSecondaryUseActive()) return super.mobInteract(player, hand);

        if (player instanceof ServerPlayer) {
            if(!setQuestNumber){
                Random random = new Random();
                this.entityData.set(QUEST_NUMBER, QuestConfig.quests.guard_quests.get(random.nextInt(QuestConfig.quests.guard_quests.size())));
                this.setQuestNumber = true;
            }

            if (this.tryAcceptGift((ServerPlayer) player, hand)) {
                player.swing(hand, true);
            } else {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.isEmpty() && ReputationHandler.getReputation(player, ReputationHandler.getFaction(ResourceLocation.tryParse("reputation:villager"))) >= 0) {
                    if (stack.isEmpty()) {
                        PlayerPatch<?> playerPatch = (PlayerPatch<?>) player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY, null).orElse(null);
                        if (!playerPatch.isBattleMode()) {
                            this.interactQuest((ServerPlayer) player, hand);
                            this.isInteracting = true;
                            this.recentInteractingPlayer = player;
                        } else {
                            (player).sendMessage(new TranslatableComponent("message.quest_giver.punch_locals"), player.getUUID());
                        }
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
                            () -> player), new OpenQuestDisplaySerializer.Message(completionDisplay, false, name, this.getQuestNumber(), this.blockPosition(), this.getId()));
                    player.swing(hand, true);
                    playRandomVillagerSound();

                } else {
                    List<SelectableQuest> active = Objects.requireNonNull(quests.getQuestLine(this.getQuestNumber())).getQuests();

                    if (active.size() == 1) {
                        QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                                () -> player), new OpenQuestDisplaySerializer.Message(active.get(0).display, false, name, this.getQuestNumber(), this.blockPosition(), this.getId()));
                        player.swing(hand, true);
                        playRandomVillagerSound();

                    } else if (!active.isEmpty()) {
                        QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                                () -> player), new OpenQuestSelectionSerializer.Message(name, this.getQuestNumber(), active, this.blockPosition(), this.getId()));
                        player.swing(hand, true);
                        playRandomVillagerSound();
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
                            () -> player), new OpenQuestDisplaySerializer.Message(initDisplay, true, name, this.getQuestNumber(), this.blockPosition(), this.getId()));
                    player.swing(hand, true);
                    playRandomVillagerSound();
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
                player.sendMessage(new TranslatableComponent("message.quest_giver.accept_gift",player.getName().getContents()), player.getUUID());
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

    protected void playRandomVillagerSound(){
        switch (random.nextInt(6)){
            case 2 -> this.playSound(SoundEvents.VILLAGER_TRADE, this.getSoundVolume(), this.getVoicePitch());
            case 3 -> this.playSound(SoundEvents.VINDICATOR_CELEBRATE, this.getSoundVolume(), this.getVoicePitch());
            case 4 -> this.playSound(SoundEvents.VILLAGER_YES, this.getSoundVolume(), this.getVoicePitch());
            case 5 -> this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
            default -> this.playSound(SoundEvents.VILLAGER_AMBIENT, this.getSoundVolume(), this.getVoicePitch());
        }
    }
}
