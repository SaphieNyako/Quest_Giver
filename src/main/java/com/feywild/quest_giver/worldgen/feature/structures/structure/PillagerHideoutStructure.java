package com.feywild.quest_giver.worldgen.feature.structures.structure;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.worldgen.feature.structures.load.StructureUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PillagerHideoutStructure extends StructureFeature<JigsawConfiguration> {
    public PillagerHideoutStructure() {
       super(JigsawConfiguration.CODEC, new PlacementFactory("pillager_hideout"));
    }

    public static boolean isFeatureChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        ChunkPos chunkpos = context.chunkPos();
        return !context.chunkGenerator().hasFeatureChunkInRange(BuiltinStructureSets.VILLAGES, context.seed(),
                chunkpos.x, chunkpos.z, 1);
    }

    @Nonnull
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    private static class PlacementFactory implements PieceGeneratorSupplier<JigsawConfiguration> {

        private final String structureId;

        private PlacementFactory(String structureId) {
            this.structureId = structureId;
        }

        @Nonnull
        @Override
        public Optional<PieceGenerator<JigsawConfiguration>> createGenerator(@Nonnull Context<JigsawConfiguration> context) {

            if (!isFeatureChunk(context)) {
                return Optional.empty();
            }

            BlockPos centerOfChunk = context.chunkPos().getMiddleBlockPosition(0);

            int landHeight = context.chunkGenerator().getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(),
                    Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

            NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());

            BlockState topBlock = columnOfBlocks.getBlock(landHeight);

            if (!topBlock.getFluidState().isEmpty()) return Optional.empty();

            JigsawConfiguration config = new JigsawConfiguration(
                    context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            .getHolder(ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY, QuestGiverMod.getInstance().resource(this.structureId)))
                            .orElseThrow(() -> new NoSuchElementException("Template not found: " + this.structureId)), 10
            );

            //Make position underground.
            BlockPos position = context.chunkPos().getMiddleBlockPosition(0); //normal pos.
            int worldSurface = context.chunkGenerator().getFirstFreeHeight(position.getX(), position.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
            int minY = context.chunkGenerator().getMinY();
            position = new BlockPos(position.getX(),
                    ((worldSurface - minY) / 2) + minY,
                    position.getZ());

            return JigsawPlacement.addPieces(
                    StructureUtils.withConfig(context, config),
                    PoolElementStructurePiece::new,
                    position,
                    false,
                    false
            );
        }
    }
}
