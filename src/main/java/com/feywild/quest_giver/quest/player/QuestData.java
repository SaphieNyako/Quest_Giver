package com.feywild.quest_giver.quest.player;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.quest.*;
import com.feywild.quest_giver.quest.task.TaskType;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestData {

    private final Map<QuestNumber, QuestLineData> questLines = new HashMap<>();
    @Nullable
    private ServerPlayer player;

    public static QuestData get(ServerPlayer player) {
        // Capability should always be there.
        // If not print a warning and get default instance
        return player.getCapability(CapabilityQuests.QUESTS).orElseGet(() -> {
            QuestGiverMod.LOGGER.warn("Quest Data capability not present on player: " + player);
            return new QuestData();
        });
    }


    // Called when the capability is attached to the player
    public void attach(ServerPlayer player) {
        this.player = player;
        this.questLines.values().forEach(l -> l.attach(player));
    }

    public boolean canComplete(QuestNumber questNumber) {
        return this.questLines.containsKey(questNumber);
    }

    @Nullable
    public QuestDisplay initialize(QuestNumber questNumber) {
        if (!this.questLines.containsKey(questNumber)) {
            QuestLine quests = QuestManager.getQuests(questNumber);

            Quest rootQuest = quests == null ? null : quests.getQuest(new ResourceLocation(QuestGiverMod.getInstance().modid, "root"));

            if (rootQuest != null) {
                QuestLineData qld = new QuestLineData(questNumber);
                if (player != null) qld.attach(player);
                this.questLines.put(questNumber, qld);
                return rootQuest.start;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void acceptQuestNumber(QuestNumber questNumber) {
        if (this.questLines.containsKey(questNumber)) {
            this.questLines.get(questNumber).approve();
        }
    }

    public void denyQuest(QuestNumber questNumber) {
        this.questLines.remove(questNumber);
    }

    public void reset() {
        this.questLines.clear();
    }

    @Nullable
    public QuestLineData getQuestLine(QuestNumber questNumber) {
        return this.questLines.getOrDefault(questNumber, null);
    }

    public Map<QuestNumber, QuestLineData> getAllQuestLines() {
        return this.questLines;
    }

    // True if the task was completed
    // Can be used so entities only accept gifts for quests
    public <T> boolean checkComplete(TaskType<?, T> type, T element) {
        boolean success = false;
        for (QuestLineData line : this.questLines.values()) {
            if (line.checkComplete(type, element)) success = true;
        }
        return success;
    }

    public <T, X> List<CompletableTaskInfo<T, X>> getAllCurrentTasks(TaskType<T, X> type) {
        ImmutableList.Builder<CompletableTaskInfo<T, X>> list = ImmutableList.builder();
        for (QuestLineData line : this.questLines.values()) {
            list.addAll(line.getAllCurrentTasks(type));
        }
        return list.build();
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        for (Map.Entry<QuestNumber, QuestLineData> entry : this.questLines.entrySet()) {
            if(entry.getKey() != null) {
                nbt.put(entry.getKey().id, entry.getValue().write());
            }
        }
        return nbt;
    }

    public void read(CompoundTag nbt) {
        this.questLines.clear();
        for (String id : nbt.getAllKeys()) {
            QuestNumber questNumber = QuestNumber.byOptionId(id);
            if (id != null) {
                QuestLineData qld = new QuestLineData(questNumber);
                qld.read(nbt.getCompound(id));
                if (player != null) qld.attach(player);
                this.questLines.put(questNumber, qld);
            }
        }
    }
}
