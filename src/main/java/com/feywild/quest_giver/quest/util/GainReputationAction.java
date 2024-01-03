package com.feywild.quest_giver.quest.util;

import net.minecraft.resources.ResourceLocation;

public class GainReputationAction {

    private ResourceLocation faction;
    private Integer reputation;

    public GainReputationAction(ResourceLocation faction, Integer reputation) {
        this.faction = faction;
        this.reputation = reputation;
    }

    public GainReputationAction(String faction, Integer reputation) {
        this.faction = new ResourceLocation(faction);
        this.reputation = reputation;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public ResourceLocation getFaction() {
        return faction;
    }

    public String getFactionPath() {
        return faction.getPath();
    }

    public void setFaction(ResourceLocation faction) {
        this.faction = faction;
    }

    public void setFaction(String faction) {
        this.faction = new ResourceLocation(faction);
    }
}
