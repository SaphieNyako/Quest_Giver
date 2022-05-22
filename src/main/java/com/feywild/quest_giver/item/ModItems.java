package com.feywild.quest_giver.item;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.entity.QuestVillager;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;

@RegisterClass
public class ModItems {

    //TODO Add other professions

    public static final Item tradingContractArmorer = new TradingContract(QuestGiverMod.getInstance(), new Item.Properties().stacksTo(1));

}
