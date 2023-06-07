package com.feywild.quest_giver.quest.task;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class KillTask extends TaggableTaskType<EntityType<?>, EntityType<?>> {

    public static final KillTask INSTANCE = new KillTask();

    private KillTask() {
        super("entity", ForgeRegistries.ENTITIES);
    }

    @Override
    public Class<EntityType<?>> testType() {
        //noinspection unchecked
        return (Class<EntityType<?>>) (Class<?>) EntityType.class;
    }

    @Override
    public boolean checkCompleted(ServerPlayer player, Taggable<EntityType<?>> element, EntityType<?> match) {
        return element.test(match);
    }
}
