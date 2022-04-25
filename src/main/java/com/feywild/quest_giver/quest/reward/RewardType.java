package com.feywild.quest_giver.quest.reward;

import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

public interface RewardType<T> {

    Class<T> element();

    void grantReward(ServerPlayer player, T element);

    T fromJson(JsonObject json);
    JsonObject toJson(T element);

}
