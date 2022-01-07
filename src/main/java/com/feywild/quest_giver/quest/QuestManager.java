package com.feywild.quest_giver.quest;

import com.google.common.collect.ImmutableMap;
import io.github.noeppi_noeppi.libx.datapack.DataLoader;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class QuestManager {

    private static Map<QuestNumber, QuestLine> questLines = ImmutableMap.of();

    public static QuestLine getQuests(QuestNumber questNumber) {
        return questLines.getOrDefault(questNumber, QuestLine.EMPTY);
    }

    public static PreparableReloadListener createReloadListener() {
        return new SimplePreparableReloadListener<Void>() {
            @Nonnull
            @Override
            protected Void prepare(@Nonnull ResourceManager rm, @Nonnull ProfilerFiller profiler) {
                return null;
            }

            @Override
            protected void apply(@Nonnull Void value, @Nonnull ResourceManager rm, @Nonnull ProfilerFiller profiler) {
                EnumMap<QuestNumber, QuestLine> lines = new EnumMap<>(QuestNumber.class);
                for (QuestNumber questNumber : QuestNumber.values()) {
                    try {

                        lines.put(questNumber, new QuestLine(DataLoader.loadJson(rm, "quests/" + questNumber.id, Quest::fromJson)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                questLines = Collections.unmodifiableMap(lines);
            }
        };
    }

}
