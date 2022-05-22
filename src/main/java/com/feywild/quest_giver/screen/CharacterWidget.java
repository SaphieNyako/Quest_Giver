package com.feywild.quest_giver.screen;


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
       return switch (number.id){
            case "quest_0001" -> VillagerProfession.ARMORER;
            case "quest_0002" -> VillagerProfession.BUTCHER;
            case "quest_0003", "quest_0023" -> VillagerProfession.CARTOGRAPHER;
           case "quest_0004" -> VillagerProfession.CLERIC;
            case "quest_0005" -> VillagerProfession.FARMER;
            case "quest_0006" -> VillagerProfession.FISHERMAN;
            case "quest_0007" -> VillagerProfession.FLETCHER;
            case "quest_0008" -> VillagerProfession.LEATHERWORKER;
            case "quest_0009" -> VillagerProfession.LIBRARIAN;
            case "quest_0010" -> VillagerProfession.MASON;
            case "quest_0011" -> VillagerProfession.SHEPHERD;
            case "quest_0012" -> VillagerProfession.TOOLSMITH;
            case "quest_0013" -> VillagerProfession.WEAPONSMITH;
            case "quest_0014" -> GuildMasterProfession.GUILDMASTER.get();
           case "quest_0015" -> ModProfessions.ENDERIAN.get();
           case "quest_0016" -> ModProfessions.ENGINEER.get();
           case "quest_0017" -> ModProfessions.FLORIST.get();
           case "quest_0018" -> ModProfessions.HUNTER.get();
           case "quest_0029" -> ModProfessions.MINER.get();
           case "quest_0020" -> ModProfessions.NETHERIAN.get();
           case "quest_0021" -> ModProfessions.OCEANOGRAPHER.get();
           case "quest_0022" -> ModProfessions.WOODWORKER.get();

           default -> VillagerProfession.NONE;
        };
    }

}
