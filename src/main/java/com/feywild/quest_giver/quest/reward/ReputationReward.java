package com.feywild.quest_giver.quest.reward;

import com.feywild.quest_giver.quest.util.GainReputationAction;
import com.google.gson.JsonObject;
import com.sun.jna.platform.win32.OaIdl;
import mods.thecomputerizer.reputation.Reputation;
import mods.thecomputerizer.reputation.api.Faction;
import mods.thecomputerizer.reputation.api.ReputationHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class ReputationReward implements RewardType<GainReputationAction> {

    public static final ReputationReward INSTANCE = new ReputationReward();

    private ReputationReward(){

    }

    @Override
    public Class<GainReputationAction> element() {
        return GainReputationAction.class;
    }

    @Override
    public void grantReward(ServerPlayer player, GainReputationAction element) {
        Collection<Faction> factions = ReputationHandler.getEntityFactions(player);

        for (Faction faction : factions) {
            if (faction.getName().equals(element.getFaction())) {
                ReputationHandler.changeReputation(player, faction, element.getReputation());
            }
        }
    }

    @Override
    public GainReputationAction fromJson(JsonObject json) {
        String loc = json.get("faction").getAsString();
        int reputation = json.get("reputation").getAsInt();
        return new GainReputationAction(loc, reputation);
    }

    @Override
    public JsonObject toJson(GainReputationAction element) {
        JsonObject json = new JsonObject();
        json.addProperty("reputation", element.getReputation());
        json.addProperty("faction", element.getFactionPath());
        return json;
    }
}
