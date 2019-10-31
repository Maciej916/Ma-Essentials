package com.maciej916.maessentials;

import com.maciej916.maessentials.config.Config;
import com.maciej916.maessentials.data.DataLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("maessentials")
public class MaEssentials
{
    public static final String MODID = "maessentials";

    public MaEssentials() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(Commands.class);
        MinecraftForge.EVENT_BUS.register(Events.class);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        Config.loadConfig();
        Config.setupMainCatalog();
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        Config.setupWorldCatalog(event);
        DataLoader.init(event);
    }
}
