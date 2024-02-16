package com.feywild.quest_giver.mixin;

import com.mojang.datafixers.util.Either;
import com.sun.jna.Structure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SinglePoolElement.class)
public interface SinglePoolElementAccessor {
    @Accessor("template")
    Either<ResourceLocation, Structure> getTemplate();
}
