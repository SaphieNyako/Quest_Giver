package com.feywild.quest_giver.screen;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.network.quest.SelectQuestSerializer;
import com.feywild.quest_giver.quest.QuestNumber;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import com.feywild.quest_giver.util.QuestGiverTextProcessor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class QuestWidget extends Button {


    public static final int WIDTH = 160;
    public static final int HEIGHT = 40;
    private QuestNumber questNumber;
    private BlockPos pos;

    public static final ResourceLocation SELECTION_TEXTURE = new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/gui/quest_background_05.png");

    private final SelectableQuest quest;
    private final ItemStack iconStack;

    public QuestWidget(int x, int y, SelectableQuest quest, QuestNumber questNumber, BlockPos pos) {
        super(x, y, WIDTH, HEIGHT, QuestGiverTextProcessor.INSTANCE.processLine(quest.display.title),b -> {});

        this.quest = quest;
        this.iconStack = new ItemStack(quest.icon);
        this.questNumber = questNumber;
        this.pos = pos;
    }

    @Override
    public void onPress() {
        super.onPress();
        QuestGiverMod.getNetwork().channel.sendToServer(new SelectQuestSerializer.Message(this.quest.id, questNumber, pos));
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {

        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SELECTION_TEXTURE);
        RenderSystem.setShaderColor(0.0F,0.0F,0.0F, 0.5F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(poseStack, this.x, this.y, 0, 0, WIDTH, HEIGHT);

        Minecraft.getInstance().getItemRenderer().renderGuiItem(this.iconStack, this.x + 8, this.y + 10);

        if ( quest.display.title.getContents().length() < 20) {
            String title_string = quest.display.title.getContents();
            drawString(poseStack, font, title_string, this.x + 30, this.y + ((HEIGHT - font.lineHeight) / 2), 0xFFFFFF);
        } else {
            String title_string = quest.display.title.getContents().substring(0, 20) + "...";
            drawString(poseStack, font, title_string, this.x + 30, this.y + ((HEIGHT - font.lineHeight) / 2), 0xFFFFFF);
        }
    }

    public boolean isHovered(int x, int y) {
        return this.x <= x && this.x + WIDTH >= x && this.y <= y && this.y + HEIGHT >= y;
    }

}
