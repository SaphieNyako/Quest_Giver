package com.feywild.quest_giver.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import net.minecraft.world.level.levelgen.structure.pools.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestGiverJigsawHelper {

    public static void registerJigsaw(MinecraftServer server, ResourceLocation poolLocation, ResourceLocation nbtLocation, int weight) {
        RegistryAccess manager = server.registryAccess();
        Registry<StructureTemplatePool> pools = manager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        StructureTemplatePool pool = pools.get(poolLocation);

        StructureProcessorList processorList = manager.registryOrThrow(Registry.PROCESSOR_LIST_REGISTRY).getOptional(poolLocation).orElse(ProcessorLists.EMPTY.value());
        List<StructurePoolElement> elements = Objects.requireNonNull(pool).templates;
            StructurePoolElement element = StructurePoolElement.legacy(nbtLocation.toString(), Holder.direct(processorList)).apply(StructureTemplatePool.Projection.RIGID);
            for (int i = 0; i < weight; i++) {
                elements.add(element);
            }

            List<Pair<StructurePoolElement, Integer>> elementCounts = new ArrayList<>(pool.rawTemplates);

            elements.addAll(pool.templates);
            elementCounts.addAll(pool.rawTemplates);

            pool.templates = elements;
            pool.rawTemplates = elementCounts;
    }
}

/*   RegistryAccess registryAccess = server.registryAccess();
        LegacySinglePoolElement piece = StructurePoolElement.legacy(nbtLocation.toString()).apply(StructureTemplatePool.Projection.RIGID);
        StructureTemplatePool pool = registryAccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).getOptional(poolLocation).orElse(null);
        if(pool != null) {

            var poolAccessor = (StructureTemplatePoolAccessor) pool;

       //     StructureProcessorList processorList = registryAccess.registryOrThrow(Registry.PROCESSOR_LIST_REGISTRY).getOptional(poolLocation).orElse(ProcessorLists.EMPTY.value());
             List<StructurePoolElement> pieces = new ArrayList<>(poolAccessor.getTemplates());

       //     StructurePoolElement element = StructurePoolElement.legacy(nbtLocation.toString(), Holder.direct(processorList)).apply(StructureTemplatePool.Projection.RIGID);
            for (int i = 0; i < weight; i++) {
                pieces.add(piece);
            }

            List<Pair<StructurePoolElement, Integer>> listOfPieces = new ArrayList(((StructureTemplatePoolAccessor) pool).getRawTemplates());

            listOfPieces.add(new Pair<>(piece, weight));
            poolAccessor.setRawTemplates(listOfPieces);
        }
    }

 */
