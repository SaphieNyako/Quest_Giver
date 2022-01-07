package com.feywild.quest_giver.screen;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import com.feywild.quest_giver.util.QuestGiverTextProcessor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
@OnlyIn(Dist.CLIENT)
public class QuestWidget extends Button {

    //TODO add widget (this is visual display)

    public static final int WIDTH = 40;
    public static final int HEIGHT = 24;

    public static final ResourceLocation SELECTION_TEXTURE = new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/gui/looking_glass.png");
    public static final ResourceLocation SLOT_TEXTURE = new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/gui/quest_atlas.png");

   // private final Alignment alignment;
    private final SelectableQuest quest;
    private final ItemStack iconStack;

    public QuestWidget(int x, int y, SelectableQuest quest) { //, Alignment alignment
        super(x, y, WIDTH, HEIGHT, QuestGiverTextProcessor.INSTANCE.processLine(quest.display.title),b -> {});
        //this.alignment = alignment;
        this.quest = quest;
        this.iconStack = new ItemStack(quest.icon);
    }

}
