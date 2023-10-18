package com.feywild.quest_giver.screen;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.events.RenderEvents;
import com.feywild.quest_giver.network.quest.ConfirmQuestSerializer;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.events.ClientEvents;
import com.feywild.quest_giver.screen.button.QuestButton;
import com.feywild.quest_giver.screen.button.QuestButtonSmall;
import com.feywild.quest_giver.screen.widget.BackgroundWidget;
import com.feywild.quest_giver.screen.widget.CharacterWidget;
import com.feywild.quest_giver.util.QuestGiverTextProcessor;
import com.feywild.quest_giver.util.RenderEnum;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.noeppi_noeppi.libx.util.ComponentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;


import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DisplayQuestScreen extends Screen {

    private final QuestDisplay display;
    private final boolean hasConfirmationButtons;
    private List<FormattedCharSequence> description;
    protected List<List<FormattedCharSequence>> textBlocks;
    private List<FormattedCharSequence> currentTextBlock;
    public int textBlockNumber;
    private final QuestNumber questNumber;
    private final BlockPos pos;

    private float xMouse;
    private float yMouse;

    int QUEST_WINDOW_POSITION_Y = 120;
    int QUEST_WINDOW_POSITION_X = 50;
    int ACCEPT_POSITION_Y = 190;
    int ACCEPT_POSITION_X = 380;
    int DECLINE_POSITION_Y = 218;
    int DECLINE_POSITION_X = 380;
    int NEXT_POSITION_Y = 120;
    int NEXT_POSITION_X = 380;
    int CHARACTER_POSITION_Y = 240;
    int CHARACTER_POSITION_X = 37;
    int DESCRIPTION_POSITION_Y = 148;
    int DESCRIPTION_POSITION_X = 70;
    int TITLE_POSITION_Y = 125;
    int WIDTH_SCREEN = 323;

    int animationCount = 0;
    int lineCount = 0;
    int secondAnimationCount = 0;
    int id;

    public DisplayQuestScreen(QuestDisplay display, boolean hasConfirmationButtons, Component name, QuestNumber questNumber, BlockPos pos, int id) {
        super(name);
        this.display = display;
        this.hasConfirmationButtons = hasConfirmationButtons;
        //TODO remove Questnumber
        this.questNumber = questNumber;
        this.pos = pos;
        this.id = id;
    }

    @Override
    protected void init() {

        this.addRenderableWidget(new BackgroundWidget(this, QUEST_WINDOW_POSITION_X, QUEST_WINDOW_POSITION_Y));
        this.addRenderableWidget(new CharacterWidget(this, CHARACTER_POSITION_X, CHARACTER_POSITION_Y, (LivingEntity) minecraft.level.getEntity(id)));

        this.description = QuestGiverTextProcessor.INSTANCE.process(this.display.description).stream().flatMap(
                line -> ComponentRenderUtils.wrapComponents(line,
                        280 , Minecraft.getInstance().font).stream()).collect(Collectors.toList());

        this.textBlocks = getTextBlocks(this.description, 8);
        this.textBlockNumber = 0;
        this.currentTextBlock = this.textBlocks.get(this.textBlockNumber);

        if(textBlocks.size() > 1) {

            this.addRenderableWidget(new QuestButtonSmall(NEXT_POSITION_X, NEXT_POSITION_Y, true, this.pos, new TextComponent(">>"), button -> {
                this.textBlockNumber++;
                reset();
                if (textBlockNumber == textBlocks.size() -1) {
                    button.setMessage(new TextComponent("x"));
                }
                if(textBlockNumber < textBlocks.size()) {
                    this.currentTextBlock = this.textBlocks.get(this.textBlockNumber);
                } else {
                    this.onClose();
                }

            }));
        }

        if (this.hasConfirmationButtons) {
            //int buttonY = Math.max((int) (this.height * (2 / 3d)), 65 + ((1 + this.description.size()) * (Minecraft.getInstance().font.lineHeight + 2)));

            TranslatableComponent msg1 = getRandomAcceptMessage();
            this.addRenderableWidget(new QuestButton(ACCEPT_POSITION_X, ACCEPT_POSITION_Y, true, this.pos, msg1, button -> {
                QuestGiverMod.getNetwork().channel.sendToServer(new ConfirmQuestSerializer.Message(true, questNumber, RenderEnum.QUESTION));
                RenderEvents.renders.put(questNumber.id, RenderEnum.QUESTION);
                this.onClose();
            }));

            TranslatableComponent msg2 = getRandomDeclineMessage();
            this.addRenderableWidget(new QuestButton(DECLINE_POSITION_X, DECLINE_POSITION_Y, false, this.pos, msg2, button -> {
                QuestGiverMod.getNetwork().channel.sendToServer(new ConfirmQuestSerializer.Message(false, questNumber, RenderEnum.EXCLAMATION));
                RenderEvents.renders.put(questNumber.id, RenderEnum.EXCLAMATION);
                this.onClose();
            }));
        }
        ClientEvents.setShowGui(false);
    }

    private TranslatableComponent getRandomAcceptMessage() {
        Random random = new Random();
        return switch (random.nextInt(5)) {
            case 1 -> new TranslatableComponent("text.quest_giver.accept1");
            case 2 -> new TranslatableComponent("text.quest_giver.accept2");
            case 3 -> new TranslatableComponent("text.quest_giver.accept3");
            case 4 -> new TranslatableComponent("text.quest_giver.accept4");
            default -> new TranslatableComponent("text.quest_giver.accept5");
        };
    }

    private TranslatableComponent getRandomDeclineMessage() {
        Random random = new Random();
        return switch (random.nextInt(5)) {
            case 1 -> new TranslatableComponent("text.quest_giver.decline1");
            case 2 -> new TranslatableComponent("text.quest_giver.decline2");
            case 3 -> new TranslatableComponent("text.quest_giver.decline3");
            case 4 -> new TranslatableComponent("text.quest_giver.decline4");
            default -> new TranslatableComponent("text.quest_giver.decline5");
        };
    }

    @Override
    public void onClose() {
        ClientEvents.setShowGui(true);
        this.currentTextBlock = this.textBlocks.get(0);
        reset();
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

            if (lineCount < getCurrentTextBlock().size()) {

                for (int i = 0; i < lineCount + 1; i++) {
                    int x = 0;
                    //show previous line
                    if (i != 0) {
                        this.minecraft.font.drawShadow(poseStack, getCurrentTextBlock().get(i - 1), this.DESCRIPTION_POSITION_X,
                                this.DESCRIPTION_POSITION_Y + ((2 + this.minecraft.font.lineHeight) * (i - 1)), 0xFFFFFF);
                    }

                    for (int j = 0; j < secondAnimationCount; j++) {

                        this.minecraft.font.drawShadow(poseStack, ComponentUtil.subSequence(getCurrentTextBlock().get(i), j, j + 1),
                                this.DESCRIPTION_POSITION_X + x,
                                this.DESCRIPTION_POSITION_Y + ((2 + this.minecraft.font.lineHeight) * i), 0xFFFFFF);

                        x += this.minecraft.font.width(ComponentUtil.subSequence(getCurrentTextBlock().get(i), j, j + 1)); //add width length.
                    }
                }

                if (this.animationCount != 0) {
                    this.animationCount++;
                } else {
                    if (secondAnimationCount < 60) {
                        secondAnimationCount++;
                    } else {
                        if (lineCount < getCurrentTextBlock().size()) {
                            lineCount++;
                        }
                        secondAnimationCount = 0;
                    }
                    animationCount = 0;
                }
            } else {
                for (int i = 0; i < getCurrentTextBlock().size(); i++) {
                    this.minecraft.font.drawShadow(poseStack, getCurrentTextBlock().get(i), this.DESCRIPTION_POSITION_X, this.DESCRIPTION_POSITION_Y + ((2 + this.minecraft.font.lineHeight) * i), 0xFFFFFF);
                }
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

    public List<List<FormattedCharSequence>> getTextBlocks(List<FormattedCharSequence> description, int numberOfMaxTextBlocks){
        int i = 0;
        List<List<FormattedCharSequence>> textBlocks = new ArrayList<>();
        while(i < description.size()){
            int nextInc = Math.min(description.size() - i, numberOfMaxTextBlocks);
            List<FormattedCharSequence> textBlock = description.subList(i, i + nextInc);
            textBlocks.add(textBlock);
            i = i + nextInc;
        }
        return textBlocks;
    }

    public List<FormattedCharSequence> getCurrentTextBlock() {
        return currentTextBlock;
    }

    public void setCurrentTextBlock(List<FormattedCharSequence> currentTextBlock) {
        this.currentTextBlock = currentTextBlock;
    }

    public void reset(){
        this.lineCount = 0;
        this.animationCount = 0;
        this.secondAnimationCount = 0;
    }
}
