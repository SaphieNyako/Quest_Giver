package com.feywild.quest_giver.screen;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.network.quest.ConfirmQuestSerializer;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.util.ClientEvents;
import com.feywild.quest_giver.util.QuestGiverTextProcessor;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayQuestScreen extends Screen {

    private final QuestDisplay display;
    private final boolean hasConfirmationButtons;
    private Component title;
    private List<FormattedCharSequence> description;
    private final QuestNumber questNumber;
    private final BlockPos pos;

    private float xMouse;
    private float yMouse;

    int QUEST_WINDOW_POSITION_Y = 120;
    int QUEST_WINDOW_POSITION_X = 50;
    int ACCEPT_POSITION_Y = 188;
    int ACCEPT_POSITION_X = 380;
    int DECLINE_POSITION_Y = 218;
    int DECLINE_POSITION_X = 380;
    int CHARACTER_POSITION_Y = 240;
    int CHARACTER_POSITION_X = 37;
    int DESCRIPTION_POSITION_Y = 150;
    int DESCRIPTION_POSITION_X = 70;
    int TITLE_POSITION_Y = 125;
    int WIDTH_SCREEN = 323;

    public DisplayQuestScreen(QuestDisplay display, boolean hasConfirmationButtons, QuestNumber questNumber, BlockPos pos) {
        super(display.title);
        this.display = display;
        this.hasConfirmationButtons = hasConfirmationButtons;
        this.questNumber = questNumber;
        this.pos = pos;
    }

    @Override
    protected void init() {

        this.addRenderableWidget(new BackgroundWidget(this, QUEST_WINDOW_POSITION_X, QUEST_WINDOW_POSITION_Y));
        this.addRenderableWidget(new CharacterWidget(this, CHARACTER_POSITION_X, CHARACTER_POSITION_Y,  minecraft.level, questNumber, pos));

        this.title = QuestGiverTextProcessor.INSTANCE.processLine(this.display.title);
        this.description = QuestGiverTextProcessor.INSTANCE.process(this.display.description).stream().flatMap(
                line -> ComponentRenderUtils.wrapComponents(line,
                        this.width - 40, Minecraft.getInstance().font).stream()).collect(Collectors.toList());

        if (this.hasConfirmationButtons) {
            //int buttonY = Math.max((int) (this.height * (2 / 3d)), 65 + ((1 + this.description.size()) * (Minecraft.getInstance().font.lineHeight + 2)));

            this.addRenderableWidget(new QuestButton(ACCEPT_POSITION_X, ACCEPT_POSITION_Y, true, this.pos, new TextComponent("accept"), button -> {
                QuestGiverMod.getNetwork().channel.sendToServer(new ConfirmQuestSerializer.Message(true, questNumber));
                this.onClose();
            }));

            this.addRenderableWidget(new QuestButton(DECLINE_POSITION_X, DECLINE_POSITION_Y, false, this.pos, new TextComponent("decline"), button -> {
                QuestGiverMod.getNetwork().channel.sendToServer(new ConfirmQuestSerializer.Message(false, questNumber));
                this.onClose();
            }));
        }
        ClientEvents.setShowGui(false);
    }

    @Override
    public void onClose() {
        ClientEvents.setShowGui(true);
        super.onClose();
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.drawTextLines(poseStack, mouseX, mouseY);
    }

    private void drawTextLines(PoseStack poseStack, int mouseX, int mouseY) {

        if (this.minecraft != null) {
            drawString(poseStack, this.minecraft.font, this.title, QUEST_WINDOW_POSITION_X + WIDTH_SCREEN / 2 - (this.minecraft.font.width(this.title) / 2), TITLE_POSITION_Y, 0xFFFFFF);
            for (int i = 0; i < this.description.size(); i++) {
                this.minecraft.font.drawShadow(poseStack, this.description.get(i), DESCRIPTION_POSITION_X, DESCRIPTION_POSITION_Y + ((2 + this.minecraft.font.lineHeight) * i), 0xFFFFFF);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !hasConfirmationButtons;
    }
}
