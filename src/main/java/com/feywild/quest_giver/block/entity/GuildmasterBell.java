package com.feywild.quest_giver.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class GuildmasterBell extends BlockEntity {

    /* THIS IS NOT IN USE - alternative idea for the Guildmaster Bell */

    @Nullable
    private UUID player = null;

    @Nullable
    private UUID guildmaster = null;

    private boolean initiated = false;

    public GuildmasterBell(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    @Nullable
    public UUID getPlayer() {
        return this.player;
    }

    public void setPlayer(@Nullable UUID player) {
        this.player = player;
    }

    @Nullable
    public UUID getGuildmaster() {
        return this.guildmaster;
    }

    public void setGuildmaster(@Nullable UUID guildmaster) {
        this.guildmaster = guildmaster;
    }

    public boolean getInitiated() {
        return this.initiated;
    }

    public void setInitiated(boolean initiated) {
        this.initiated = initiated;
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.player = nbt.hasUUID("playerId") ? nbt.getUUID("playerId") : null;
        this.guildmaster = nbt.hasUUID("guildmasterId") ? nbt.getUUID("guildmasterId") : null;
        this.initiated = nbt.getBoolean("initiated");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("initiated", this.initiated);
        if (this.player == null) {
            nbt.remove("playerId");
        } else {
            nbt.putUUID("playerId", this.player);
        }
        if (this.guildmaster == null) {
            nbt.remove("guildmasterId");
        } else {
            nbt.putUUID("guildmasterId", this.guildmaster);
        }
    }
}
