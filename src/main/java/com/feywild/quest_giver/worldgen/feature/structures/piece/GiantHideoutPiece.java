package com.feywild.quest_giver.worldgen.feature.structures.piece;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

public class GiantHideoutPiece extends BaseStructurePiece{

    public static final StructurePoolElementType<GiantHideoutPiece> TYPE = type(GiantHideoutPiece::new);

    protected GiantHideoutPiece(Either<ResourceLocation, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection) {
        super(template, processors, projection);
    }

    @NotNull
    @Override
    public StructurePoolElementType<?> getType() {
        return TYPE;
    }
}
