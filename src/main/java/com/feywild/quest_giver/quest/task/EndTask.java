package com.feywild.quest_giver.quest.task;

import com.feywild.quest_giver.entity.QuestVillager;
import com.feywild.quest_giver.quest.QuestNumber;
import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.IForgeRegistry;

public class EndTask implements TaskType<QuestNumber, QuestVillager> {

    public static final EndTask INSTANCE = new EndTask();

    private EndTask(){

    }

    @Override
    public Class<QuestNumber> element() {
        return QuestNumber.class;
    }

    @Override
    public Class<QuestVillager> testType() {
        return QuestVillager.class;
    }

    @Override
    public boolean checkCompleted(ServerPlayer player, QuestNumber element, QuestVillager match) {
        return element == match.getQuestNumber();
    }

    @Override
    public QuestNumber fromJson(JsonObject json) {
              return QuestNumber.byId(json.get("quest_number").getAsString());
    }

    @Override
    public JsonObject toJson(QuestNumber element) {
        JsonObject json = new JsonObject();
        json.addProperty("quest_number", element.id);
        return null;
    }
}
