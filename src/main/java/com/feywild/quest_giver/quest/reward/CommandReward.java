package com.feywild.quest_giver.quest.reward;


import com.google.gson.JsonObject;

import net.minecraft.server.level.ServerPlayer;


public class CommandReward implements RewardType<String>{

    public static final CommandReward INSTANCE = new CommandReward();

    private CommandReward(){

    }

    @Override
    public Class<String> element() {
        return String.class;
    }

    @Override
    public void grantReward(ServerPlayer player, String element) {
            //If the problem is @p
            //String s = element.replace("@p", player.getName().toString());
            player.server.getCommands().performCommand(player.server.createCommandSourceStack(), element);

    }

    @Override
    public String fromJson(JsonObject json) {
        return json.get("command").getAsString();
    }

    @Override
    public JsonObject toJson(String element) {
        JsonObject json = new JsonObject();
        json.addProperty("command", element);
        return json;
    }
}
