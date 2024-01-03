package com.feywild.quest_giver.block;

import com.feywild.quest_giver.QuestGiverMod;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import io.github.noeppi_noeppi.libx.base.BlockBase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

@RegisterClass
public class ModBlocks {

    public static final Block guildmasterBell = new BlockBase(
            QuestGiverMod.getInstance(),
            BlockBehaviour.Properties.copy(Blocks.CARTOGRAPHY_TABLE)
                    .strength(-1, 3600000)
                    .noDrops()
                    .noOcclusion()
                    .randomTicks()
                    .noCollission()
                    .sound(SoundType.LANTERN));
}
