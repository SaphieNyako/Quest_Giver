package com.feywild.quest_giver.config;

import io.github.noeppi_noeppi.libx.config.Config;
import io.github.noeppi_noeppi.libx.config.validator.IntRange;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpawnConfig {
    public static final class GuildConfig {
        @Config("Guild weight in villages. Will work if max is 0 - default 1")
        @IntRange(min = 0, max = 16)
        public static int weight = 3;

        @Config("Max guild amount in village - default 1")
        @IntRange(min = 0, max = 8)
        public static int max = 1;

        @Config("Min guild amount in village - default 1")
        @IntRange(min = 0, max = 8)
        public static int min = 1;

        @Config("Guild spawn structures config for villages. The guild will be random generated from the entries")
        public static Map<String, Set<String>> add_structure_piece = new HashMap<>() {
            {
                put(
                        "minecraft:village/desert/houses",
                        Set.of("village/desert/houses/guild_house", "village/desert/houses/stall"));
                put(
                        "minecraft:village/plains/houses",
                        Set.of("village/plains/houses/guild_house", "village/plains/houses/stall"));
                put(
                        "minecraft:village/savanna/houses",
                        Set.of("village/savanna/houses/guild_house", "village/savanna/houses/stall"));
                put(
                        "minecraft:village/snowy/houses",
                        Set.of("village/snow/houses/guild_house", "village/snow/houses/stall"));
                put(
                        "minecraft:village/taiga/houses",
                        Set.of("village/taiga/houses/guild_house", "village/taiga/houses/stall"));
            }
        };
    }

    @Config("Quest Villager spawn weight - default 15")
    public static int quest_villager_weight = 15;

    @Config("Quest Guard spawn weight - default 5")
    public static int quest_guard_weight = 5;
}
