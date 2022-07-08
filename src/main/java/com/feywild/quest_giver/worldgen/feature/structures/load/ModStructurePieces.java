package com.feywild.quest_giver.worldgen.feature.structures.load;

import com.feywild.quest_giver.QuestGiverMod;
import com.feywild.quest_giver.worldgen.feature.structures.piece.UndergroundPrisonPiece;
import net.minecraft.core.Registry;

public class ModStructurePieces {

    public static void setup() {
        Registry.register(Registry.STRUCTURE_POOL_ELEMENT, QuestGiverMod.getInstance().resource("underground_prison"), UndergroundPrisonPiece.TYPE);

    }

}
