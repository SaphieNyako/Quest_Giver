package com.feywild.quest_giver.tag;

import com.feywild.quest_giver.QuestGiverMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public class ModTags {

    public static final TagKey<Item> INK = ItemTags.create(QuestGiverMod.getInstance().resource("inks"));

    public static class ConfiguredStructureFeatures {
        public static final TagKey<ConfiguredStructureFeature<?, ?>> PILLAGER_HIDEOUT_TAG = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(QuestGiverMod.getInstance().modid, "pillager_hideout"));
        public static final TagKey<ConfiguredStructureFeature<? ,?>> PILLAGER_BASE_TAG = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(QuestGiverMod.getInstance().modid, "pillager_base"));
        public static final TagKey<ConfiguredStructureFeature<? ,?>> CAVE_DWELLING_TAG = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(QuestGiverMod.getInstance().modid, "cave_dwelling"));
        public static final TagKey<ConfiguredStructureFeature<? ,?>> GIANT_DWELLING_TAG = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(QuestGiverMod.getInstance().modid, "giant_hideout"));
    }

}
