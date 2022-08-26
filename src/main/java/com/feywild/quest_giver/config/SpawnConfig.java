package com.feywild.quest_giver.config;

import io.github.noeppi_noeppi.libx.config.Config;

public class SpawnConfig {

    @Config("guild house spawn rate - default 3")
    public static int guild_house_spawn_rate = 3;

    @Config("guild stall spawn rate - default 3")
    public static int guild_stall_spawn_rate = 3;

    @Config("Quest Villager spawn weight - default 15")
    public static int quest_villager_weight = 15;

    @Config("Quest Guard spawn weight - default 5")
    public static int quest_guard_weight = 5;

}
