package com.feywild.quest_giver.util;

import io.github.noeppi_noeppi.libx.util.TextProcessor;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class QuestGiverTextProcessor extends TextProcessor {

    public static final QuestGiverTextProcessor INSTANCE = new QuestGiverTextProcessor();

    private QuestGiverTextProcessor() {}

    @Override
    public Style customCommand(Style style, String command) {
        if ("green".equalsIgnoreCase(command)) {
            return style.withColor(TextColor.fromRgb(0x66cc99));
        } else if ("gold".equalsIgnoreCase(command)) {
            return style.withColor(TextColor.fromRgb(0xffcc00));
        } else if ("red".equalsIgnoreCase(command)) {
            return style.withColor(TextColor.fromRgb(0xcc3333));
        } else if ("blue".equalsIgnoreCase(command)) {
            return style.withColor(TextColor.fromRgb(0x66ccff));
        } else {
            return super.customCommand(style, command);
        }
    }
}
