package com.feywild.quest_giver.block;

import com.feywild.quest_giver.block.entity.GuildmasterBell;
import com.feywild.quest_giver.entity.GuildMasterProfession;
import com.feywild.quest_giver.entity.ModEntityTypes;
import com.feywild.quest_giver.entity.QuestVillager;
import io.github.noeppi_noeppi.libx.base.tile.BlockBE;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GuildmasterBellBlock extends BlockBE<GuildmasterBell> {

    /* THIS IS NOT IN USE - alternative idea for the Guildmaster Bell */

    public static final VoxelShape SHAPE = box(5.1875, 0, 5.26563, 10.8125, 3.23438, 10.70313);

    public GuildmasterBellBlock(ModX mod) {
        super(
                mod,
                GuildmasterBell.class,
                BlockBehaviour.Properties.copy(Blocks.CARTOGRAPHY_TABLE)
                        .strength(-1, 3600000)
                        .noDrops()
                        .noOcclusion()
                        .randomTicks()
                        .noCollission()
                        .sound(SoundType.LANTERN));
    }

    @Override
    public void onRemove(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState newState,
            boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(
            @Nonnull BlockState state,
            @Nonnull BlockGetter levelIn,
            @Nonnull BlockPos pos,
            @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(
            @Nonnull BlockState state,
            Level level,
            @Nonnull BlockPos pos,
            @Nonnull Player player,
            @Nonnull InteractionHand hand,
            @Nonnull BlockHitResult trace) {
        if (level.isClientSide) {
            level.playSound(player, pos, SoundEvents.NOTE_BLOCK_BELL, SoundSource.BLOCKS, 1f, 1.2f);
        } else {
            GuildmasterBell blockEntity = this.getBlockEntity(level, pos);
            if (level instanceof ServerLevel) {
                Entity guildmaster = blockEntity.getGuildmaster() != null
                        ? ((ServerLevel) level).getEntity(blockEntity.getGuildmaster())
                        : null;
                if (guildmaster != null && guildmaster.isAlive()) {
                    if (guildmaster instanceof Villager) {
                        ((Villager) guildmaster).releaseAllPois();
                        guildmaster.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
                // Spawn Guildmaster
                QuestVillager entity = new QuestVillager(ModEntityTypes.questVillager, level);
                VillagerData villagerData = new VillagerData(
                        VillagerType.byBiome(level.getBiome(pos)), GuildMasterProfession.GUILDMASTER.get(), 1);
                entity.setVillagerData(villagerData);
                entity.setVillagerXp(1);
                entity.setPos(pos.getX() - 0.5, pos.getY(), pos.getZ() - 0.5);
                for (Direction dir : Direction.values()) {
                    if (dir.getAxis() != Direction.Axis.Y) {
                        BlockPos target = pos.below().relative(dir);
                        if (level.getBlockState(target).isAir()) {
                            entity.setPos(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
                            break;
                        }
                    }
                }
                level.addFreshEntity(entity);
                blockEntity.setGuildmaster(entity.getUUID());
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
