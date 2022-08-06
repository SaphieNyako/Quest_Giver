package com.feywild.quest_giver.entity;

import com.feywild.quest_giver.EventListener;
import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.config.QuestConfig;
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
import mods.thecomputerizer.reputation.api.ReputationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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
            Random random = new Random();
            if (villagerprofession == VillagerProfession.ARMORER) {
                setQuestNumber(QuestConfig.quests.armorer_quests.get(random.nextInt(QuestConfig.quests.armorer_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.BUTCHER) {
                setQuestNumber(QuestConfig.quests.butcher_quests.get(random.nextInt(QuestConfig.quests.butcher_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.CARTOGRAPHER) {
               setQuestNumber(QuestConfig.quests.cartographer_quests.get(random.nextInt(QuestConfig.quests.cartographer_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.CLERIC) {
                setQuestNumber(QuestConfig.quests.cleric_quests.get(random.nextInt(QuestConfig.quests.cleric_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.FARMER) {
                setQuestNumber(QuestConfig.quests.farmer_quests.get(random.nextInt(QuestConfig.quests.farmer_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.FISHERMAN) {
                setQuestNumber(QuestConfig.quests.fisherman_quests.get(random.nextInt(QuestConfig.quests.fisherman_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.FLETCHER) {
                setQuestNumber(QuestConfig.quests.fletcher_quests.get(random.nextInt(QuestConfig.quests.fletcher_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.LEATHERWORKER) {
                setQuestNumber(QuestConfig.quests.leatherworker_quests.get(random.nextInt(QuestConfig.quests.leatherworker_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.LIBRARIAN) {
                setQuestNumber(QuestConfig.quests.librarian_quests.get(random.nextInt(QuestConfig.quests.librarian_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.MASON) {
                setQuestNumber(QuestConfig.quests.mason_quests.get(random.nextInt(QuestConfig.quests.mason_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.SHEPHERD) {
                setQuestNumber(QuestConfig.quests.shepherd_quests.get(random.nextInt(QuestConfig.quests.shepherd_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.TOOLSMITH) {
                setQuestNumber(QuestConfig.quests.toolsmith_quests.get(random.nextInt(QuestConfig.quests.toolsmith_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == VillagerProfession.WEAPONSMITH) {
                setQuestNumber(QuestConfig.quests.weaponsmith_quests.get(random.nextInt(QuestConfig.quests.weaponsmith_quests.size())));
                this.setVillagerXp(1);
            }

            //GUILDMASTER
            if (villagerprofession == GuildMasterProfession.GUILDMASTER.get()) {
                setQuestNumber(QuestConfig.quests.guildmaster_quests.get(random.nextInt(QuestConfig.quests.guildmaster_quests.size())));
                this.setVillagerXp(1);
            }

            //MORE VILLAGER PROFESSIONS
            if (villagerprofession == ModProfessions.ENDERIAN.get()) {
                setQuestNumber(QuestConfig.quests.enderian_quests.get(random.nextInt(QuestConfig.quests.enderian_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.ENGINEER.get()) {
                setQuestNumber(QuestConfig.quests.engineer_quests.get(random.nextInt(QuestConfig.quests.engineer_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.FLORIST.get()) {
                setQuestNumber(QuestConfig.quests.florist_quests.get(random.nextInt(QuestConfig.quests.florist_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.HUNTER.get()) {
                setQuestNumber(QuestConfig.quests.hunter_quests.get(random.nextInt(QuestConfig.quests.hunter_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.MINER.get()) {
                setQuestNumber(QuestConfig.quests.miner_quests.get(random.nextInt(QuestConfig.quests.miner_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.NETHERIAN.get()) {
                setQuestNumber(QuestConfig.quests.netherian_quests.get(random.nextInt(QuestConfig.quests.netherian_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.OCEANOGRAPHER.get()) {
                setQuestNumber(QuestConfig.quests.oceanographer_quests.get(random.nextInt(QuestConfig.quests.oceanographer_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == ModProfessions.WOODWORKER.get()) {
                setQuestNumber(QuestConfig.quests.woodworker_quests.get(random.nextInt(QuestConfig.quests.woodworker_quests.size())));
                this.setVillagerXp(1);
            }
            if (villagerprofession == com.lupicus.bk.entity.ModProfessions.BEEKEEPER) {
                setQuestNumber(25);
                this.setVillagerXp(1);
            }

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
        this.setQuestNumber = true;
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
    	if (player.isSecondaryUseActive()) return super.mobInteract(player, hand);
        if (player instanceof ServerPlayer) {
            if (this.tryAcceptGift((ServerPlayer) player, hand)) {
                player.swing(hand, true);
            } else {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.isEmpty() && ReputationHandler.getReputation(player, ReputationHandler.getFaction(ResourceLocation.tryParse("reputation:villager"))) >= 0) {
                    PlayerPatch<?> playerPatch = (PlayerPatch<?>) player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY, null).orElse(null);
                    if(!playerPatch.isBattleMode()) {
                        this.interactQuest((ServerPlayer) player, hand);
                        this.setTradingPlayer(player);
                    } else {
                        (player).sendMessage(new TextComponent("Please be careful not to punch the locals! Please Leave Battle Mode before interacting."), player.getUUID());
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
                    playRandomVillagerSound();

                } else {
                    List<SelectableQuest> active = Objects.requireNonNull(quests.getQuestLine(this.getQuestNumber())).getQuests();

                    if (active.size() == 1) {
                        QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                                () -> player), new OpenQuestDisplaySerializer.Message(active.get(0).display, false, name, this.getQuestNumber(), this.blockPosition()));
                        player.swing(hand, true);
                        playRandomVillagerSound();

                    } else if (!active.isEmpty()) {
                        QuestGiverMod.getNetwork().channel.send(PacketDistributor.PLAYER.with(
                                () -> player), new OpenQuestSelectionSerializer.Message(name, this.getQuestNumber(), active, this.blockPosition()));
                        player.swing(hand, true);
                        playRandomVillagerSound();
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
                    playRandomVillagerSound();
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
