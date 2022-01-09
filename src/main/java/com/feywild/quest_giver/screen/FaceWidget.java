package com.feywild.quest_giver.screen;

import com.feywild.quest_giver.QuestGiverMod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;


public class FaceWidget extends Button {

    public static final int WIDTH = 33;
    public static final int HEIGHT = 32;

    public static final ResourceLocation FACE = new ResourceLocation(QuestGiverMod.getInstance().modid, "textures/gui/face_villager.png");

    public FaceWidget(int x, int y) {
        super(x, y, WIDTH ,HEIGHT, new TextComponent(" "), B -> {});
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShaderTexture(0, FACE);
        this.blit(poseStack, this.x, this.y, 0, 0, WIDTH, HEIGHT);
    }

}
