package com.feywild.quest_giver.screen;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.quest.util.SelectableQuest;
import com.feywild.quest_giver.util.QuestGiverTextProcessor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.noeppi_noeppi.libx.screen.Panel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class BackgroundWidget extends Panel {

    public static final int WIDTH = 390;
    public static final int HEIGHT = 119;

    public static final ResourceLocation BACKGROUND_TEXTURE_01 = new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/gui/quest_background_01.png");
    public static final ResourceLocation BACKGROUND_TEXTURE_02 = new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/gui/quest_background_02.png");


    public BackgroundWidget(Screen screen, int x, int y) {
        super(screen ,x, y, WIDTH ,HEIGHT);

    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE_01);
        this.blit(poseStack, this.x, this.y, 0, 0, 240, HEIGHT);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE_02);
        this.blit(poseStack, this.x + 240, this.y, 0, 0, 140, HEIGHT);

    }
}
