package com.feywild.quest_giver.config;

import io.github.noeppi_noeppi.libx.config.Config;

import java.util.List;

public class QuestConfig {

    public static class quests {

        @Config("Armorer Quest numbers")
        public static List<Integer> armorer_quests = List.of(1);

        @Config("Butcher Quest numbers")
        public static List<Integer> butcher_quests = List.of(2);

        @Config("Cartographer Quest numbers")
        public static List<Integer> cartographer_quests = List.of(3);

        @Config("Cleric Quest numbers")
        public static List<Integer> cleric_quests = List.of(4);

        @Config("Farmer Quest numbers")
        public static List<Integer> farmer_quests = List.of(5);

        @Config("Fisherman Quest numbers")
        public static List<Integer> fisherman_quests = List.of(6);

        @Config("Fletcher Quest numbers")
        public static List<Integer> fletcher_quests = List.of(7);

        @Config("Leatherworker Quest numbers")
        public static List<Integer> leatherworker_quests = List.of(8);

        @Config("Librarian Quest numbers")
        public static List<Integer> librarian_quests = List.of(9);

        @Config("Mason Quest numbers")
        public static List<Integer> mason_quests = List.of(10);

        @Config("Shepherd Quest numbers")
        public static List<Integer> shepherd_quests = List.of(11);

        @Config("Toolsmith Quest numbers")
        public static List<Integer> toolsmith_quests = List.of(12);

        @Config("Weaponsmith Quest numbers")
        public static List<Integer> weaponsmith_quests = List.of(13);

        @Config("Guildmaster Quest numbers")
        public static List<Integer> guildmaster_quests = List.of(14);

        @Config("Enderian Quest numbers")
        public static List<Integer> enderian_quests = List.of(15);

        @Config("Engineer Quest numbers")
        public static List<Integer> engineer_quests = List.of(16);

        @Config("Florist Quest numbers")
        public static List<Integer> florist_quests = List.of(17);

        @Config("Hunter Quest numbers")
        public static List<Integer> hunter_quests = List.of(18);

        @Config("Miner Quest numbers")
        public static List<Integer> miner_quests = List.of(19);

        @Config("Netherian Quest numbers")
        public static List<Integer> netherian_quests = List.of(20);

        @Config("Oceanographer Quest numbers")
        public static List<Integer> oceanographer_quests = List.of(21);

        @Config("Woodworker Quest numbers")
        public static List<Integer> woodworker_quests = List.of(22);

        @Config("Guard Quest numbers")
        public static List<Integer> guard_quests = List.of(23);

        @Config("Beekeeper Quest numbers")
        public static List<Integer> beekeeper_quests = List.of(25);
    }
}
