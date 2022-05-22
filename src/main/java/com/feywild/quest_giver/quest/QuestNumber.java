package com.feywild.quest_giver.quest;

import com.feywild.quest_giver.util.RenderEnum;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum QuestNumber {

    QUEST_0000("quest_0000"),
    QUEST_0001("quest_0001"),
    QUEST_0002("quest_0002"),
    QUEST_0003("quest_0003"),
    QUEST_0004("quest_0004"),
    QUEST_0005("quest_0005"),
    QUEST_0006("quest_0006"),
    QUEST_0007("quest_0007"),
    QUEST_0008("quest_0008"),
    QUEST_0009("quest_0009"),
    QUEST_0010("quest_0010"),
    QUEST_0011("quest_0011"),
    QUEST_0012("quest_0012"),
    QUEST_0013("quest_0013"),
    QUEST_0014("quest_0014"),
    QUEST_0015("quest_0015"),
    QUEST_0016("quest_0016"),
    QUEST_0017("quest_0017"),
    QUEST_0018("quest_0018"),
    QUEST_0019("quest_0019"),
    QUEST_0020("quest_0020"),
    QUEST_0021("quest_0021"),
    QUEST_0022("quest_0022"),
    QUEST_0023("quest_0023"),
    QUEST_0024("quest_0024");

    public final String id;

    QuestNumber(String id) {
        this.id = id;

    }

    public static QuestNumber byId(String id) {
        switch (id.toLowerCase(Locale.ROOT).trim()) {
            case "quest_0000": return QUEST_0000;
            case "quest_0001": return QUEST_0001;
            case "quest_0002": return QUEST_0002;
            case "quest_0003": return QUEST_0003;
            case "quest_0004": return QUEST_0004;
            case "quest_0005": return QUEST_0005;
            case "quest_0006": return QUEST_0006;
            case "quest_0007": return QUEST_0007;
            case "quest_0008": return QUEST_0008;
            case "quest_0009": return QUEST_0009;
            case "quest_0010": return QUEST_0010;
            case "quest_0011": return QUEST_0011;
            case "quest_0012": return QUEST_0012;
            case "quest_0013": return QUEST_0013;
            case "quest_0014": return QUEST_0014;
            case "quest_0015": return QUEST_0015;
            case "quest_0016": return QUEST_0016;
            case "quest_0017": return QUEST_0017;
            case "quest_0018": return QUEST_0018;
            case "quest_0019": return QUEST_0019;
            case "quest_0020": return QUEST_0020;
            case "quest_0021": return QUEST_0021;
            case "quest_0022": return QUEST_0022;
            case "quest_0023": return QUEST_0023;
            case "quest_0024": return QUEST_0024;
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
