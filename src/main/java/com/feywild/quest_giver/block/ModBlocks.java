package com.feywild.quest_giver.block;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.block.entity.GuildmasterBell;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import io.github.noeppi_noeppi.libx.base.BlockBase;
import io.github.noeppi_noeppi.libx.base.tile.BlockBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

@RegisterClass
public class ModBlocks {

    public static final BlockBE<GuildmasterBell> guildmasterBell = new GuildmasterBellBlock(QuestGiverMod.getInstance());

}
