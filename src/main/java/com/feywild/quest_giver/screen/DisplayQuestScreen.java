package com.feywild.quest_giver.screen;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.network.quest.ConfirmQuestSerializer;
import com.feywild.quest_giver.quest.Quest;
import com.feywild.quest_giver.quest.QuestDisplay;
import com.feywild.quest_giver.util.QuestGiverTextProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.stream.Collectors;

public class DisplayQuestScreen extends Screen {

    private final QuestDisplay display;
    private final boolean hasConfirmationButtons;
    private Component title;
    private List<FormattedCharSequence> description;

    public DisplayQuestScreen(QuestDisplay display, boolean hasConfirmationButtons) {
        super(display.title);
        this.display = display;
        this.hasConfirmationButtons = hasConfirmationButtons;
    }

    @Override
    protected void init() {
        super.init();
        this.title = QuestGiverTextProcessor.INSTANCE.processLine(this.display.title);
        this.description = QuestGiverTextProcessor.INSTANCE.process(this.display.description).stream().flatMap(
                line -> ComponentRenderUtils.wrapComponents(line,
                        this.width - 40, Minecraft.getInstance().font).stream()).collect(Collectors.toList());

        if (this.hasConfirmationButtons) {
            int buttonY = Math.max((int) (this.height * (2 / 3d)), 65 + ((1 + this.description.size()) * (Minecraft.getInstance().font.lineHeight + 2)));
            this.addRenderableWidget(new Button(30, buttonY, 20, 20, new TextComponent(Character.toString((char) 0x2714)), button -> {
                QuestGiverMod.getNetwork().channel.sendToServer(new ConfirmQuestSerializer.Message(true));
                this.onClose();
            }));
            this.addRenderableWidget(new Button(70, buttonY, 20, 20, new TextComponent("x"), button -> {
                QuestGiverMod.getNetwork().channel.sendToServer(new ConfirmQuestSerializer.Message(false));
                this.onClose();
            }));
        }
    }



}
