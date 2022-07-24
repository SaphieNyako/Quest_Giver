package com.feywild.quest_giver.worldgen.feature.structures.load;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.worldgen.feature.structures.piece.*;
import net.minecraft.core.Registry;

public class ModStructurePieces {

    public static void setup() {
        Registry.register(Registry.STRUCTURE_POOL_ELEMENT, QuestGiverMod.getInstance().resource("pillager_hideout"), PillagerHideoutPiece.TYPE);
        Registry.register(Registry.STRUCTURE_POOL_ELEMENT, QuestGiverMod.getInstance().resource("cave_dwelling"), CaveDwellingPiece.TYPE);
        Registry.register(Registry.STRUCTURE_POOL_ELEMENT, QuestGiverMod.getInstance().resource("pillager_base"), PillagerBasePiece.TYPE);
        Registry.register(Registry.STRUCTURE_POOL_ELEMENT, QuestGiverMod.getInstance().resource("giant_hideout"), GiantHideoutPiece.TYPE);

    }

}
