package com.feywild.quest_giver.screen;


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
import net.minecraft.world.entity.EntityType;
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

    public QuestNumber number;
    public Level level;
    private float xMouse;
    private float yMouse;
    private final Villager villager;
    private final Guard guard;


    public CharacterWidget(Screen screen, int x, int y, Level level, QuestNumber number, BlockPos pos) {
        super(screen ,x, y, WIDTH ,HEIGHT);
        this.level = level;
        this.number = number;
        this.villager = setVillager(number, pos);
        this.guard = setGuard(number, pos);

    }

    private Guard setGuard(QuestNumber number, BlockPos pos) {
        return new Guard(ModEntityTypes.questGuardVillager, this.level);
    }

    private Villager setVillager(QuestNumber number, BlockPos pos) {

            Villager villager = new Villager(EntityType.VILLAGER, this.level);
            VillagerData villagerData = new VillagerData(VillagerType.byBiome(level.getBiome(pos)), getProfession(number), 1);
            villager.setVillagerData(villagerData);
            return villager;


    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {

        this.xMouse = (float)mouseX;
        this.yMouse = (float)mouseY;
        if(Objects.equals(number.id, "quest_0023") || Objects.equals(number.id, "quest_0024")) {
            InventoryScreen.renderEntityInInventory(this.x, this.y, 65,  (float) 0 - this.xMouse, (float)100 - this.yMouse, guard);
        } else {
            InventoryScreen.renderEntityInInventory(this.x, this.y, 65,  (float) 0 - this.xMouse, (float)100 - this.yMouse, villager);
        }
    }

    private VillagerProfession getProfession(QuestNumber number) {

        VillagerProfession profession = VillagerProfession.NONE;

        try {
            String questNumber = number.id.replace("quest_", "");
            Integer numberId = Integer.parseInt(questNumber);

            if (QuestConfig.quests.armorer_quests.contains(numberId)) {
                profession = VillagerProfession.ARMORER;
            }
            if (QuestConfig.quests.butcher_quests.contains(numberId)) {
                profession = VillagerProfession.BUTCHER;
            }
            if (QuestConfig.quests.cartographer_quests.contains(numberId)) {
                profession = VillagerProfession.CARTOGRAPHER;
            }
            if (QuestConfig.quests.cleric_quests.contains(numberId)) {
                profession = VillagerProfession.CLERIC;
            }
            if (QuestConfig.quests.farmer_quests.contains(numberId)) {
                profession = VillagerProfession.FARMER;
            }
            if (QuestConfig.quests.fisherman_quests.contains(numberId)) {
                profession = VillagerProfession.FISHERMAN;
            }
            if (QuestConfig.quests.fletcher_quests.contains(numberId)) {
                profession = VillagerProfession.FLETCHER;
            }
            if (QuestConfig.quests.leatherworker_quests.contains(numberId)) {
                profession = VillagerProfession.LEATHERWORKER;
            }
            if (QuestConfig.quests.librarian_quests.contains(numberId)) {
                profession = VillagerProfession.LIBRARIAN;
            }
            if (QuestConfig.quests.mason_quests.contains(numberId)) {
                profession = VillagerProfession.MASON;
            }
            if (QuestConfig.quests.shepherd_quests.contains(numberId)) {
                profession = VillagerProfession.SHEPHERD;
            }
            if (QuestConfig.quests.toolsmith_quests.contains(numberId)) {
                profession = VillagerProfession.TOOLSMITH;
            }
            if (QuestConfig.quests.weaponsmith_quests.contains(numberId)) {
                profession = VillagerProfession.WEAPONSMITH;
            }
            if (QuestConfig.quests.guildmaster_quests.contains(numberId)) {
                profession = GuildMasterProfession.GUILDMASTER.get();
            }
            if (QuestConfig.quests.enderian_quests.contains(numberId)) {
                profession = ModProfessions.ENDERIAN.get();
            }
            if (QuestConfig.quests.engineer_quests.contains(numberId)) {
                profession = ModProfessions.ENGINEER.get();
            }
            if (QuestConfig.quests.florist_quests.contains(numberId)) {
                profession = ModProfessions.ENDERIAN.get();
            }
            if (QuestConfig.quests.hunter_quests.contains(numberId)) {
                profession = ModProfessions.HUNTER.get();
            }
            if (QuestConfig.quests.miner_quests.contains(numberId)) {
                profession = ModProfessions.MINER.get();
            }
            if (QuestConfig.quests.netherian_quests.contains(numberId)) {
                profession = ModProfessions.NETHERIAN.get();
            }
            if (QuestConfig.quests.oceanographer_quests.contains(numberId)) {
                profession = ModProfessions.OCEANOGRAPHER.get();
            }
            if (QuestConfig.quests.woodworker_quests.contains(numberId)) {
                profession = ModProfessions.WOODWORKER.get();
            }
            if (QuestConfig.quests.beekeeper_quests.contains(numberId)) {
                profession = com.lupicus.bk.entity.ModProfessions.BEEKEEPER;
            }
        }
        catch (Exception e){
            return profession;
        }

        return profession;
    }

}
