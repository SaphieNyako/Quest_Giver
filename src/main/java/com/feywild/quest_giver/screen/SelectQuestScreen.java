package com.feywild.quest_giver.screen;

import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.List;

public class SelectQuestScreen extends Screen {

    //TODO: remove all GUI's when active
    private final List<SelectableQuest> quests;
    private QuestNumber questNumber;
    private BlockPos pos;

    public SelectQuestScreen(Component name,  List<SelectableQuest> quests, QuestNumber questNumber, BlockPos pos) {
        super(name);

        this.quests = ImmutableList.copyOf(quests);
        this.questNumber = questNumber;
        this.pos = pos;
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < this.quests.size(); i++) {
            this.addRenderableWidget(new QuestWidget(20, 40 + ((QuestWidget.HEIGHT + 4) * i), this.quests.get(i), questNumber, pos));
        }
    }


    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.drawTextLines(poseStack, mouseX, mouseY);
    }

    private void drawTextLines(PoseStack poseStack, int mouseX, int mouseY) {
        if (this.minecraft != null) {
            drawString(poseStack, this.minecraft.font, this.title, this.width / 2 - (this.minecraft.font.width(this.title) / 2), 10, 0xFFFFFF);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
