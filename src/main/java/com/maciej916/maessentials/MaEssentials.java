package com.maciej916.maessentials;

import com.maciej916.maessentials.config.ConfigHolder;
import com.maciej916.maessentials.data.DataLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MaEssentials.MODID)
public class MaEssentials {
    public static final String MODID = "ma-essentials";

    public MaEssentials() {
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC, MODID + ".toml");

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(Commands.class);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DataLoader.setupMain(event);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        DataLoader.setupWorld(event);
        DataLoader.load();
    }
}
