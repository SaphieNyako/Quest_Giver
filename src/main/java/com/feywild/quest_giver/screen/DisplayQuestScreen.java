package com.feywild.quest_giver.screen;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.network.quest.ConfirmQuestSerializer;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.util.QuestGiverTextProcessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayQuestScreen extends Screen {

    private final QuestDisplay display;
    private final boolean hasConfirmationButtons;
    private Component title;
    private List<FormattedCharSequence> description;
    private final QuestNumber questNumber;


    private float xMouse;
    private float yMouse;

    public DisplayQuestScreen(QuestDisplay display, boolean hasConfirmationButtons, QuestNumber questNumber) {
        super(display.title);
        this.display = display;
        this.hasConfirmationButtons = hasConfirmationButtons;
        this.questNumber = questNumber;
    }

    @Override
    protected void init() {
        super.init();

        int QUEST_WINDOW_POSITION_Y = 100;
        int QUEST_WINDOW_POSITION_X = 50;
        int ACCEPT_POSITION_Y = 185;
        int ACCEPT_POSITION_X = 150;
        int DECLINE_POSITION_Y = 185;
        int DECLINE_POSITION_X = 250;
        int FACE_POSITION_Y = 84;
        int FACE_POSITION_X = 380;


        this.addRenderableWidget(new BackgroundWidget(this, QUEST_WINDOW_POSITION_X, QUEST_WINDOW_POSITION_Y));
        this.addRenderableWidget(new FaceWidget(FACE_POSITION_X, FACE_POSITION_Y));

        this.title = QuestGiverTextProcessor.INSTANCE.processLine(this.display.title);
        this.description = QuestGiverTextProcessor.INSTANCE.process(this.display.description).stream().flatMap(
                line -> ComponentRenderUtils.wrapComponents(line,
                        this.width - 40, Minecraft.getInstance().font).stream()).collect(Collectors.toList());

        if (this.hasConfirmationButtons) {
            //int buttonY = Math.max((int) (this.height * (2 / 3d)), 65 + ((1 + this.description.size()) * (Minecraft.getInstance().font.lineHeight + 2)));


            this.addRenderableWidget(new Button(ACCEPT_POSITION_X, ACCEPT_POSITION_Y, 80, 20, new TextComponent("accept"), button -> {
                QuestGiverMod.getNetwork().channel.sendToServer(new ConfirmQuestSerializer.Message(true, questNumber));
                this.onClose();
            }));

            this.addRenderableWidget(new Button(DECLINE_POSITION_X, DECLINE_POSITION_Y, 80, 20, new TextComponent("decline"), button -> {
                QuestGiverMod.getNetwork().channel.sendToServer(new ConfirmQuestSerializer.Message(false, questNumber));
                this.onClose();
            }));

           // InventoryScreen.renderEntityInInventory(50, 60, 17, (float) 50 - this.xMouse, (float)(75 - 50) - this.yMouse, villager);
        }


    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            //  this.renderBackground(poseStack);
            // this.xMouse = (float)mouseX;
            // this.yMouse = (float)mouseY;
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.drawTextLines(poseStack, mouseX, mouseY);


    }

    private void drawTextLines(PoseStack poseStack, int mouseX, int mouseY) {
        int DESCRIPTION_POSITION_Y = 110;
        int DESCRIPTION_POSITION_X = 60;

        if (this.minecraft != null) {
            drawString(poseStack, this.minecraft.font, this.title, this.width / 2 - (this.minecraft.font.width(this.title) / 2), 20, 0xFFFFFF);
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
