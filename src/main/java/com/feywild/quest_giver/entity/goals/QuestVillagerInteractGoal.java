package com.feywild.quest_giver.entity.goals;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class QuestVillagerInteractGoal extends Goal {

    private final Entity entity;
    private Player target;
    private boolean interacting;

    public QuestVillagerInteractGoal(Entity entity, boolean interacting){
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return interacting;
    }

    @Override
    public boolean canContinueToUse() {
        return interacting;
    }

    @Override
    public void start() {
        this.target = null;
        AABB box = new AABB(this.entity.blockPosition()).inflate(4);
        for (Player match : this.entity.level.getEntities(EntityType.PLAYER, box, e -> !e.isSpectator())) {
            this.target = match;
            break;
        }
    }

    @Override
    public void tick() {
        this.entity.lookAt(EntityAnchorArgument.Anchor.EYES, this.target.position());
        this.entity.moveTo(this.target.position());
    }
}
