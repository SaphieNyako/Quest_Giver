package com.feywild.quest_giver.screen.widget;


import com.feywild.quest_giver.config.QuestConfig;
import com.feywild.quest_giver.entity.GuildMasterProfession;
import com.feywild.quest_giver.entity.ModEntityTypes;
import com.feywild.quest_giver.quest.QuestNumber;
import com.samebutdifferent.morevillagers.init.ModProfessions;
import io.github.noeppi_noeppi.libx.screen.Panel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.Level;
import tallestegg.guardvillagers.entities.Guard;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;


public class CharacterWidget extends Panel {

    public static final int WIDTH = 33;
    public static final int HEIGHT = 32;

    private final LivingEntity entity;


    public CharacterWidget(Screen screen, int x, int y, LivingEntity entity) {
        super(screen ,x, y, WIDTH ,HEIGHT);
        this.entity = entity;
    }


    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {

        float xMouse = (float) mouseX;
        float yMouse = (float) mouseY;

            InventoryScreen.renderEntityInInventory(this.x, this.y, 65,
                    (float) 0 - xMouse, (float) 100 - yMouse, this.entity);

    }
}
