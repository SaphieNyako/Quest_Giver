package com.feywild.quest_giver;

import io.github.noeppi_noeppi.libx.mod.registration.ModXRegistration;
import io.github.noeppi_noeppi.libx.mod.registration.RegistrationBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
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
    //    private static QuestGiverNetWork network;

    public QuestGiverMod() {

        instance = this;
        //test
       //STILL REQUIRED?
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Nonnull
    public static QuestGiverMod getInstance(){
        return instance;
    }

    //TODO Network

    @Override
    protected void initRegistration(RegistrationBuilder builder) {
        builder.setVersion(1);
    }

    @Override
    protected void setup(final FMLCommonSetupEvent event)
    {

    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public void reloadData(AddReloadListenerEvent event) {
        //TODO Add Quest Manager
    }

}
