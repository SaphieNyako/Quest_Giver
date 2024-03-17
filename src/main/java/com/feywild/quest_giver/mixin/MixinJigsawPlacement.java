package com.feywild.quest_giver.mixin;

import com.feywild.quest_giver.config.SpawnConfig;
import com.feywild.quest_giver.util.LimitedJigsawPlacement$Placer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// https://github.com/LordDeatHunter/FabricWaystones/blob/master/src/main/java/wraith/fwaystones/mixin/StructurePoolBasedGeneratorMixin.java
@Mixin(JigsawPlacement.class)
public class MixinJigsawPlacement {
    @Inject(
            remap = false,
            method = {"lambda$addPieces$0", "m_210268_"},
            at = @At(value = "INVOKE", target = "Ljava/util/Deque;addLast(Ljava/lang/Object;)V"))
    private static void preGenerate2(
            PoolElementStructurePiece p_210269_,
            JigsawConfiguration p_210270_,
            int p_210271_,
            int p_210272_,
            int p_210273_,
            Registry p_210274_,
            JigsawPlacement.PieceFactory p_210275_,
            ChunkGenerator p_210276_,
            StructureManager p_210277_,
            WorldgenRandom random,
            BoundingBox p_210279_,
            boolean p_210280_,
            LevelHeightAccessor p_210281_,
            StructurePiecesBuilder p_210282_,
            PieceGenerator.Context p_210283_,
            CallbackInfo ci,
            @Local JigsawPlacement.Placer placer) {
        int delta = SpawnConfig.GuildConfig.max - SpawnConfig.GuildConfig.min;
        int guildCount;
        if (delta == 0) {
            guildCount = SpawnConfig.GuildConfig.min;
        } else if (delta < 0 || SpawnConfig.GuildConfig.max == 0) {
            guildCount = 0;
        } else
            guildCount = SpawnConfig.GuildConfig.min
                    + random.next(SpawnConfig.GuildConfig.max - SpawnConfig.GuildConfig.min);
        ((LimitedJigsawPlacement$Placer) (Object) placer).questgiver$guildCount(guildCount);
    }
}
