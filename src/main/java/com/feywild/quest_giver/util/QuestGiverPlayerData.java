package com.feywild.quest_giver.util;

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

public class QuestGiverPlayerData implements INBTSerializable<Tag> {

    private static final String KEY = "QuestGiverData";
    private static List<String> questList = new ArrayList<>();

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

    public static void addToList(String string){
        System.out.println(getQuestList().size());
        getQuestList().add(string);
        System.out.println(getQuestList().size());
    }

    public static List<String> getQuestList() {
        return questList;
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        ListTag quests = new ListTag();
        for (String quest : questList) {
            quests.add(StringTag.valueOf(quest));
        }
        nbt.put("Quests", quests);
        return nbt;
    }

    public void read(CompoundTag nbt){
        ListTag quests = nbt.getList("Quests", Tag.TAG_STRING);
        questList.clear();
        for (int i = 0; i < quests.size(); i++) {
            String id = quests.getString(i);
            questList.add(id);
        }
    }

    @Override
    public Tag serializeNBT() {
        return this.write();
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if(nbt instanceof CompoundTag tag){
         this.read(tag);
        }
    }
}
