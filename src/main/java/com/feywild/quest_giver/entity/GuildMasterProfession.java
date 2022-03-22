package com.feywild.quest_giver.entity;

import com.feywild.quest_giver.QuestGiverMod;
import com.google.common.collect.ImmutableSet;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class GuildMasterProfession {

    public static final DeferredRegister<VillagerProfession> PROFESSION = DeferredRegister.create(ForgeRegistries.PROFESSIONS, QuestGiverMod.getInstance().modid);

    public static final RegistryObject<VillagerProfession> GUILDMASTER = PROFESSION.register("guildmaster",
            ()-> new VillagerProfession("guildmaster", ModPoiTypes.GUILDMASTER_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_LIBRARIAN));



}
