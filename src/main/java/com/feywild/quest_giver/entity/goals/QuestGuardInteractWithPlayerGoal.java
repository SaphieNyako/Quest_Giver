package com.feywild.quest_giver.entity.goals;

import com.feywild.quest_giver.entity.QuestGuardVillager;
import com.feywild.quest_giver.util.ServerPlayerGuiStatus;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class QuestGuardInteractWithPlayerGoal extends Goal {

    private final QuestGuardVillager guard;

    public QuestGuardInteractWithPlayerGoal(QuestGuardVillager guard) {
        this.guard = guard;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    private boolean checkConditions() {
        if (!this.guard.isAlive()) {
            return false;
        } else if (this.guard.isInWater()) {
            return false;
        } else if (!this.guard.isOnGround()) {
            return false;
        } else if (this.guard.hurtMarked) {
            return false;
        } else {
            Player player = this.guard.getInteractingPlayer();
            if (player == null) {
                return false;
            } else if (this.guard.distanceToSqr(player) > 16.0D) {
                return false;
            } else {
                this.guard.getLookControl().setLookAt(this.guard.getInteractingPlayer().getX(), this.guard.getInteractingPlayer().getEyeY(), this.guard.getInteractingPlayer().getZ());
                return !ServerPlayerGuiStatus.playersNotInteractingWithAQuestGui.contains(this.guard.getInteractingPlayer().getUUID());
            }
        }
    }

    @Override
    public boolean canUse() {
        return checkConditions();
    }

    @Override
    public boolean canContinueToUse() {
        return checkConditions();
    }

    public void start() {
        this.guard.getNavigation().stop();
    }

    public void stop() {
        this.guard.setInteractingPlayer(null);
    }
}
