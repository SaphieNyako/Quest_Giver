package com.feywild.quest_giver.quest;

import com.feywild.quest_giver.quest.reward.RewardType;
import com.feywild.quest_giver.quest.reward.RewardTypes;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class QuestReward {

    private final RewardType<Object> reward;
    private final Object element;
    private final int times;

    private QuestReward(RewardType<Object> reward, Object element, int times) {
        this.reward = reward;
        this.element = element;
        this.times = times;
        if (!this.reward.element().isAssignableFrom(element.getClass())) {
            throw new IllegalStateException("Can't create quest task: element type mismatch");
        }
    }

    public static <T> QuestReward of(RewardType<T> type, T element) {
        //noinspection unchecked
        return new QuestReward((RewardType<Object>) type, element, 1);
    }

    public static <T> QuestReward of(RewardType<T> type, T element, int times) {
        //noinspection unchecked
        return new QuestReward((RewardType<Object>) type, element, times);
    }

    public void grantReward(ServerPlayer player) {
        for (int i = 0; i < times; i++) {
            this.reward.grantReward(player, this.element);
        }
    }

    public JsonObject toJson() {
        JsonObject json = this.reward.toJson(this.element);
        json.addProperty("id", RewardTypes.getId(this.reward).toString());
        json.addProperty("times", times);
        return json;
    }

    public static QuestReward fromJson(JsonObject json) {
        //noinspection unchecked
        RewardType<Object> reward = (RewardType<java.lang.Object>)
                RewardTypes.getType(new ResourceLocation(json.get("id").getAsString()));
        Object element = reward.fromJson(json);
        int times = json.get("times").getAsInt();
        return new QuestReward(reward, element, times);
    }
}
