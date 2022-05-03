package com.feywild.quest_giver.util;

import com.feywild.quest_giver.QuestGiverMod;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;
public enum RenderEnum {
    EXCLAMATION("exclamation", new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/entity/quest_villager/exclamation_mark.png")),
    QUESTION("question", new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/entity/quest_villager/question_mark.png")),
    NONE("none", null);

    private final String id;
    private final ResourceLocation render;

    RenderEnum(String id, ResourceLocation render) {
        this.id = id;
        this.render = render;
    }

    public static RenderEnum getRender(String id) {
        return switch (id.toLowerCase(Locale.ROOT).trim()) {
            case "exclamation" -> EXCLAMATION;
            case "question" -> QUESTION;
            default -> NONE;
        };
    }

    public String getId() {
        return this.id;
    }

    public ResourceLocation getRender() {
        return this.render;
    }
}
