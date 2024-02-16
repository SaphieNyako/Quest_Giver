package com.feywild.quest_giver.util;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.config.SpawnConfig;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.*;

// https://github.com/LordDeatHunter/FabricWaystones/blob/1.18.2/src/main/java/wraith/fwaystones/util/Utils.java#L83C4-L109C6
public final class GuildGenUtils {
    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY =
            ResourceKey.create(Registry.PROCESSOR_LIST_REGISTRY, new ResourceLocation("minecraft", "empty"));

    public static final HashSet<ResourceLocation> GUILD_STRUCTURES = new HashSet<>();

    public static final Map<ResourceLocation, Set<ResourceLocation>> VILLAGES_TO_GUILD = new HashMap<>();

    static {
        GUILD_STRUCTURES.add(ResourceLocation.tryParse("quest_giver:village/desert/houses/guild_house"));
        GUILD_STRUCTURES.add(ResourceLocation.tryParse("quest_giver:village/desert/houses/stall"));
        GUILD_STRUCTURES.add(ResourceLocation.tryParse("quest_giver:village/plains/houses/guild_house"));
        GUILD_STRUCTURES.add(ResourceLocation.tryParse("quest_giver:village/plains/houses/stall"));
        GUILD_STRUCTURES.add(ResourceLocation.tryParse("quest_giver:village/savanna/houses/guild_house"));
        GUILD_STRUCTURES.add(ResourceLocation.tryParse("quest_giver:village/savanna/houses/stall"));
        GUILD_STRUCTURES.add(ResourceLocation.tryParse("quest_giver:village/snow/houses/guild_house"));
        GUILD_STRUCTURES.add(ResourceLocation.tryParse("quest_giver:village/snow/houses/stall"));
        GUILD_STRUCTURES.add(ResourceLocation.tryParse("quest_giver:village/taiga/houses/guild_house"));
        GUILD_STRUCTURES.add(ResourceLocation.tryParse("quest_giver:village/taiga/houses/stall"));

        for (Map.Entry<String, Set<String>> entry : SpawnConfig.GuildConfig.add_structure_piece.entrySet()) {
            for (String value : entry.getValue()) {
                final var set = VILLAGES_TO_GUILD.computeIfAbsent(
                        ResourceLocation.tryParse(entry.getKey()), (key) -> new HashSet<>());
                set.add(new ResourceLocation(QuestGiverMod.getInstance().modid, value));
            }
        }
    }

    public static void addToStructurePool(
            MinecraftServer server, ResourceLocation village, ResourceLocation guild, int weight) {

        Holder<StructureProcessorList> emptyProcessorList = server.registryAccess()
                .registryOrThrow(Registry.PROCESSOR_LIST_REGISTRY)
                .getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY);

        var poolGetter = server.registryAccess()
                .registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                .getOptional(village);

        if (poolGetter.isEmpty()) {
            QuestGiverMod.LOGGER.error("Cannot add to " + village + " as it cannot be found!");
            return;
        }
        var pool = poolGetter.get();

        var pieceList = pool.templates;
        var piece = StructurePoolElement.single(guild.toString(), emptyProcessorList)
                .apply(StructureTemplatePool.Projection.RIGID);

        var list = new ArrayList<>(pool.rawTemplates);
        list.add(Pair.of(piece, weight));
        pool.rawTemplates = list;

        for (int i = 0; i < weight; ++i) {
            pieceList.add(piece);
        }
    }

    public static void registerVillage(MinecraftServer server, ResourceLocation village, ResourceLocation guild) {
        QuestGiverMod.LOGGER.info("Adding guild " + guild.toString() + " to village " + village.toString());
        addToStructurePool(server, village, guild, 3);
    }

    public static void registerGuildVillageWorldgen(MinecraftServer server) {
        for (var entry : VILLAGES_TO_GUILD.entrySet()) {
            for (ResourceLocation location : entry.getValue()) {
                registerVillage(server, entry.getKey(), location);
            }
        }
    }
}
