package com.feywild.quest_giver.item;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.entity.QuestVillager;
import com.samebutdifferent.morevillagers.init.ModProfessions;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;

@RegisterClass
public class ModItems {

    //TODO Add other professions

    public static final Item tradingContractArmorer = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1),"armorer");
    public static final Item tradingContractButcher = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "butcher");
    public static final Item tradingContractCartographer = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "cartographer");
    public static final Item tradingContractCleric = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "cleric");
    public static final Item tradingContractFarmer = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "farmer");
    public static final Item tradingContractFisherman = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "fisherman");
    public static final Item tradingContractLeatherworker = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "leatherworker");
    public static final Item tradingContractLibrarian = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "librarian");
    public static final Item tradingContractMason = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "mason");
    public static final Item tradingContractShepherd = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "shepherd");
    public static final Item tradingContractToolsmith = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "toolsmith");
    public static final Item tradingContractWeaponsmith = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "weaponsmith");

    //Guildmaster has no trades
    public static final Item tradingContractEnderian = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1),"enderian");
    public static final Item tradingContractEngineer = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "engineer");
    public static final Item tradingContractFlorist = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "florist");
    public static final Item tradingContractHunter = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "hunter");
    public static final Item tradingContractNetherian = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "netherian");
    public static final Item tradingContractOceanographer = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "oceanographer");
    public static final Item tradingContractWoodworker = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "woodworker");
    //Guards have no trades
    public static final Item tradingContractBeekeeper = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1), "beekeeper");

}
