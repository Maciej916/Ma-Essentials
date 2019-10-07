package com.maciej916.maessentials;

import com.maciej916.maessentials.setup.ClientProxy;
import com.maciej916.maessentials.setup.Config;
import com.maciej916.maessentials.setup.IProxy;
import com.maciej916.maessentials.setup.ServerProxy;
import com.maciej916.maessentials.managers.HomeManager;
import com.maciej916.maessentials.managers.WarpManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("maessentials")
public class MaEssentials {
    public static final String MODID = "maessentials";

    public static final Logger logger = LogManager.getLogger("[MaEssentials] ");
    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    private static Config config = new Config();
    private static HomeManager homeManager = new HomeManager();
    private static WarpManager warpManager = new WarpManager();

    public MaEssentials() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(new Events());
        MinecraftForge.EVENT_BUS.register(new Commands());
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        logger.info("Pre init...");
        proxy.init();
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        config.init(event);

        homeManager.init();
        warpManager.init();
    }

}