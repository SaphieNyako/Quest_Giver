package com.feywild.quest_giver.util;

import com.feywild.quest_giver.quest.QuestNumber;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.ArrayUtils;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestGiverPlayerData {

    private static final String KEY = "QuestGiverData";
    private static final String NUMBER_ACTIVE_QUESTS = "number_active_quests";
    private static final String ACTIVE_QUEST = "active_quest_";

    public static CompoundTag get(Player player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(KEY, Tag.TAG_COMPOUND)) {
            persistent.put(KEY, new CompoundTag());
        }
        return persistent.getCompound(KEY);
    }

    public static void copy(Player source, Player target) {
        if (source.getPersistentData().contains(KEY, Tag.TAG_COMPOUND)) {
            target.getPersistentData().put(KEY, source.getPersistentData().getCompound(KEY).copy());
        }
    }

    public static void addToList(Player player, QuestNumber questNumber){
        if (QuestGiverPlayerData.get(Objects.requireNonNull(player)).getInt(NUMBER_ACTIVE_QUESTS) == 0){  // If active quest number is 0 , replace with a boolean HAS_ACTIVE_QUESTS?
            QuestGiverPlayerData.get(player).putInt(NUMBER_ACTIVE_QUESTS, 1);
            QuestGiverPlayerData.get(player).putString(ACTIVE_QUEST + 1, questNumber.id);
            System.out.println(QuestGiverPlayerData.get(player).getString(ACTIVE_QUEST + 1));
        } else {
            int number_active_quests = QuestGiverPlayerData.get(Objects.requireNonNull(player)).getInt(NUMBER_ACTIVE_QUESTS) + 1;
            QuestGiverPlayerData.get(player).putInt(NUMBER_ACTIVE_QUESTS, number_active_quests); // save new number of active quests.
            QuestGiverPlayerData.get(player).putString(ACTIVE_QUEST + number_active_quests, questNumber.id);
            System.out.println(QuestGiverPlayerData.get(player).getString(ACTIVE_QUEST + number_active_quests));
        }
    }

    public static List<String> getQuestList(Player player) {
        List<String> questList = new ArrayList<>();

        System.out.println( QuestGiverPlayerData.get(Objects.requireNonNull(player)).getInt(NUMBER_ACTIVE_QUESTS));

        for (int i = 1; i < QuestGiverPlayerData.get(Objects.requireNonNull(player)).getInt(NUMBER_ACTIVE_QUESTS) + 1; i++){
            String questNumber = QuestGiverPlayerData.get(player).getString(ACTIVE_QUEST + i);
            questList.add(questNumber);
            System.out.println(questNumber);
        }

        return questList;
    }

    public static boolean checkQuestList(Player player, QuestNumber questNumber){
        boolean questNumberPresent = false;

        System.out.println(QuestGiverPlayerData.get(player).getInt(NUMBER_ACTIVE_QUESTS));
        System.out.println(QuestGiverPlayerData.get(player).getInt(NUMBER_ACTIVE_QUESTS) + 1);
        for (int i = 1; i < QuestGiverPlayerData.get(Objects.requireNonNull(player)).getInt(NUMBER_ACTIVE_QUESTS) + 1; i++) {
            questNumberPresent = QuestGiverPlayerData.get(player).getString(ACTIVE_QUEST + i).equals(questNumber.id);
            if(questNumberPresent) break;
        }
        return questNumberPresent;
    }

    //TODO removed from List


}
