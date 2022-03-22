package com.feywild.quest_giver.entity;

import com.feywild.quest_giver.QuestGiverMod;

import com.feywild.quest_giver.block.ModBlocks;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;


public class ModPoiTypes {

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, QuestGiverMod.getInstance().modid);

   public static final RegistryObject<PoiType> GUILDMASTER_POI = POI_TYPES.register("guildmaster",
           () -> new PoiType("guildmaster", PoiType.getBlockStates(ModBlocks.guildmasterBell),1,1));

   public static void register(){
       for (RegistryObject<PoiType> poi : POI_TYPES.getEntries()) {
           try {
               ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class).invoke(null, poi.get());
           } catch (InvocationTargetException | IllegalAccessException e) {
               e.printStackTrace();
           }
       }
   }

}
