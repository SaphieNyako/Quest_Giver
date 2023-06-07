package com.feywild.quest_giver.quest.task;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class BiomeTask extends TaggableTaskType<Biome, Biome> {

    public static final BiomeTask INSTANCE = new BiomeTask();

    private BiomeTask() {
        super("biome", ForgeRegistries.BIOMES);
    }

    @Override
    public Class<Biome> testType() {
        return Biome.class;
    }

    @Override
    public boolean checkCompleted(ServerPlayer player, Taggable<Biome> element, Biome match) {
        return element.tag().map(it -> it.contains(match)).orElseGet(() -> Objects.requireNonNull(
                        element.value().orElseThrow().getRegistryName())
                .equals(match.getRegistryName()));
    }
}
