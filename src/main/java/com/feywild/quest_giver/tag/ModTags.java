package com.feywild.quest_giver.tag;

import com.feywild.quest_giver.QuestGiverMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public class ModTags {

    public static class ConfiguredStructureFeatures {
        public static final TagKey<ConfiguredStructureFeature<?, ?>> QUEST_STRUCTURE_TAG = tag();

        private static TagKey<ConfiguredStructureFeature<?, ?>> tag() {
            return TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(QuestGiverMod.getInstance().modid, "custom_structure_tag"));
        }
    }

}
