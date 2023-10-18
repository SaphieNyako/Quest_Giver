package com.feywild.quest_giver.mixin;

import com.feywild.quest_giver.util.GuildGenUtils;
import com.feywild.quest_giver.util.LimitedJigsawPlacement$Placer;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Mixin(JigsawPlacement.Placer.class)
public class MixinJigsawPlacement$Placer implements LimitedJigsawPlacement$Placer {
    @Shadow
    @Final
    private List<? super PoolElementStructurePiece> pieces;

    @Unique
    private int questgiver$guildCount = 0;

    @Unique
    private static boolean questgiver$isGuild(StructurePoolElement element) {
        return element instanceof SinglePoolElement singlePoolElement
                && GuildGenUtils.GUILD_STRUCTURES.contains(((SinglePoolElementAccessor) singlePoolElement)
                        .getTemplate()
                        .left()
                        .orElse(new ResourceLocation("empty")));
    }

    @Override
    public LimitedJigsawPlacement$Placer questgiver$guildCount(int questgiver$guildCount) {
        this.questgiver$guildCount = questgiver$guildCount;
        return this;
    }

    @WrapOperation(
            method = "tryPlacingChildren",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/levelgen/structure/pools/StructureTemplatePool;getShuffledTemplates(Ljava/util/Random;)Ljava/util/List;",
                            ordinal = 0))
    private List<StructurePoolElement> questgiver$limitGuildCount(
            StructureTemplatePool pool, Random random, Operation<List<StructurePoolElement>> original) {
        if (questgiver$guildCount == 0
                || !GuildGenUtils.VILLAGES_TO_GUILD.containsKey(
                        pool.getName())) return original.call(pool, random);
        long count = pieces.stream()
                .filter(it -> it instanceof PoolElementStructurePiece piece && questgiver$isGuild(piece.getElement()))
                .count();
        final var needGenerating = questgiver$guildCount > count;
        final var result = original.call(pool, random).stream()
                .filter(it -> needGenerating == questgiver$isGuild(it))
                .toList();
        return result;
    }
}
