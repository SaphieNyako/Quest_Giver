package com.feywild.quest_giver;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;

public class WorldLoader {

    public static void loadWorld(BiomeLoadingEvent event){
        Random random = new Random();
        @Nullable
        ResourceLocation biomeId = event.getName();
        if (biomeId == null) return;

        ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, biomeId);
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        mobSpawns(event, biomeId, types, random);

    }

    private static void mobSpawns(BiomeLoadingEvent event, ResourceLocation biomeId, Set<BiomeDictionary.Type> types, Random random) {
        if (types.contains(BiomeDictionary.Type.OVERWORLD) && !types.contains(BiomeDictionary.Type.OCEAN) ) {

        }
    }
}
