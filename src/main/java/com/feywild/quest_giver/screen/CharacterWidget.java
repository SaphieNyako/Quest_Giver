package com.feywild.quest_giver.screen;


import com.feywild.quest_giver.entity.ModEntityTypes;
import com.feywild.quest_giver.entity.QuestVillager;
import io.github.noeppi_noeppi.libx.screen.Panel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.Level;
import javax.annotation.Nonnull;
import java.awt.*;


public class CharacterWidget extends Panel {

    public static final int WIDTH = 33;
    public static final int HEIGHT = 32;

    public Level level;
    private float xMouse;
    private float yMouse;

    public CharacterWidget(Screen screen, int x, int y, Level level) {
        super(screen ,x, y, WIDTH ,HEIGHT);
        this.level = level;
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {

        this.xMouse = (float)mouseX;
        this.yMouse = (float)mouseY;

        QuestVillager villager = new QuestVillager(ModEntityTypes.questVillager, this.level);
        DisplayQuestScreen.renderEntityInInventory(this.x, this.y, 65,  (float) 50 - this.xMouse, (float)(75 - 50) - this.yMouse, villager);
    }

}
