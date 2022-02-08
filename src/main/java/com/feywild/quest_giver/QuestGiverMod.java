package com.feywild.quest_giver;

import com.feywild.quest_giver.entity.ModEntityTypes;
import com.feywild.quest_giver.entity.QuestVillager;
import com.feywild.quest_giver.network.QuestGiverNetwork;
import com.feywild.quest_giver.quest.QuestManager;
import com.feywild.quest_giver.quest.player.CapabilityQuests;
import com.feywild.quest_giver.quest.reward.ItemReward;
import com.feywild.quest_giver.quest.reward.RewardTypes;
import com.feywild.quest_giver.quest.task.CraftTask;
import com.feywild.quest_giver.quest.task.KillTask;
import com.feywild.quest_giver.quest.task.TaskTypes;
import io.github.noeppi_noeppi.libx.mod.registration.ModXRegistration;
import io.github.noeppi_noeppi.libx.mod.registration.RegistrationBuilder;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;


@Mod("quest_giver")
public final class QuestGiverMod extends ModXRegistration
{

    private static QuestGiverMod instance;
    private static QuestGiverNetwork network;

    public QuestGiverMod() {

        instance = this;
        network = new QuestGiverNetwork(this);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(CapabilityQuests::register);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::entityAttributes);

        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, CapabilityQuests::attachPlayerCaps);
        MinecraftForge.EVENT_BUS.addListener(CapabilityQuests::playerCopy);

        MinecraftForge.EVENT_BUS.register(new EventListener());

        // Quest task & reward types. Not in setup as they are required for datagen.
        TaskTypes.register(new ResourceLocation(this.modid, "craft"), CraftTask.INSTANCE);
        TaskTypes.register(new ResourceLocation(this.modid, "kill"), KillTask.INSTANCE);

        RewardTypes.register(new ResourceLocation(this.modid, "item"), ItemReward.INSTANCE);

       //STILL REQUIRED?
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Nonnull
    public static QuestGiverMod getInstance(){
        return instance;
    }

    @Nonnull
    public static QuestGiverNetwork getNetwork() {
        return network;
    }

    @Override
    protected void initRegistration(RegistrationBuilder builder) {
        builder.setVersion(1);
    }

    @Override
    protected void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            SpawnPlacements.register(ModEntityTypes.questVillager, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, QuestVillager::canSpawn);
        });
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void clientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntityTypes.questVillager, VillagerRenderer::new);
    }

    @SubscribeEvent
    public void reloadData(AddReloadListenerEvent event) {
        event.addListener(QuestManager.createReloadListener());
    }

    private void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.questVillager, QuestVillager.createAttributes().build());
    }

}
