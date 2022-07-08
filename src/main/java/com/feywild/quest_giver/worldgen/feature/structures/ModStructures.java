package com.feywild.quest_giver.worldgen.feature.structures;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.worldgen.feature.structures.structure.UndergroundPrisonStructure;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURE_FEATURE_DEFERRED_REGISTERD_REGISTRY =
            DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, QuestGiverMod.getInstance().modid);

    public static final RegistryObject<StructureFeature<?>> UNDERGROUND_PRISON =
            STRUCTURE_FEATURE_DEFERRED_REGISTERD_REGISTRY.register("underground_prison", UndergroundPrisonStructure::new);


    public static void register(IEventBus eventBus) {
        STRUCTURE_FEATURE_DEFERRED_REGISTERD_REGISTRY.register(eventBus);
    }

}
