package com.feywild.quest_giver.worldgen.feature.structures;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.worldgen.feature.structures.piece.PillagerBasePiece;
import com.feywild.quest_giver.worldgen.feature.structures.piece.PillagerHideoutPiece;
import com.feywild.quest_giver.worldgen.feature.structures.structure.CaveDwellingStructure;
import com.feywild.quest_giver.worldgen.feature.structures.structure.GiantHideoutStructure;
import com.feywild.quest_giver.worldgen.feature.structures.structure.PillagerBaseStructure;
import com.feywild.quest_giver.worldgen.feature.structures.structure.PillagerHideoutStructure;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURE_FEATURE_DEFERRED_REGISTERD_REGISTRY =
            DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, QuestGiverMod.getInstance().modid);


    public static final RegistryObject<StructureFeature<?>> CAVE_DWELLING =
            STRUCTURE_FEATURE_DEFERRED_REGISTERD_REGISTRY.register("cave_dwelling", CaveDwellingStructure::new);

    public static final RegistryObject<StructureFeature<?>> GIANT_HIDEOUT =
            STRUCTURE_FEATURE_DEFERRED_REGISTERD_REGISTRY.register("giant_hideout", GiantHideoutStructure::new);

    public static final RegistryObject<StructureFeature<?>> PILLAGER_BASE =
            STRUCTURE_FEATURE_DEFERRED_REGISTERD_REGISTRY.register("pillager_base", PillagerBaseStructure::new);

    public static final RegistryObject<StructureFeature<?>> PILLAGER_HIDEOUT =
            STRUCTURE_FEATURE_DEFERRED_REGISTERD_REGISTRY.register("pillager_hideout", PillagerHideoutStructure::new);


    public static void register(IEventBus eventBus) {
        STRUCTURE_FEATURE_DEFERRED_REGISTERD_REGISTRY.register(eventBus);
    }

}
