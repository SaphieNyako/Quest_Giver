package com.feywild.quest_giver.sound;

import com.feywild.quest_giver.QuestGiverMod;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

@RegisterClass
public class ModSoundEvents {

    public static final SoundEvent villagerDecline = new SoundEvent(new ResourceLocation(QuestGiverMod.getInstance().modid, "villager_decline"));
    public static final SoundEvent villagerAccept = new SoundEvent(new ResourceLocation(QuestGiverMod.getInstance().modid, "villager_accept"));
}
