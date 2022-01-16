package com.feywild.quest_giver.quest;

import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum QuestNumber {

    QUEST_0001("quest_0001"),
    QUEST_0002("quest_0002"),
    QUEST_0003("quest_0003");

    public final String id;


    QuestNumber(String id) {

        this.id = id;

    }

    public static QuestNumber byId(String id) {
        switch (id.toLowerCase(Locale.ROOT).trim()) {
            case "quest_0001": return QUEST_0001;
            case "quest_0002": return QUEST_0002;
            case "quest_0003": return QUEST_0003;
            default: throw new IllegalArgumentException("Invalid quest number: " + id);
        }

    }

    public static String optionId(@Nullable QuestNumber questNumber) {
        return questNumber == null ? "unassigned" : questNumber.id;
    }

    @Nullable
    public static QuestNumber byOptionId(String id) {
        try {
            return byId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
