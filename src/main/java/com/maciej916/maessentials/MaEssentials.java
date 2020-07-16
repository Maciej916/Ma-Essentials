package com.maciej916.maessentials;

import com.maciej916.maessentials.common.config.ConfigHolder;
import com.maciej916.maessentials.common.data.DataLoader;
import com.maciej916.maessentials.common.network.Networking;
import com.maciej916.maessentials.common.proxy.ClientProxy;
import com.maciej916.maessentials.common.proxy.IProxy;
import com.maciej916.maessentials.common.proxy.ServerProxy;
import com.maciej916.maessentials.common.register.ModCommands;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MaEssentials.MODID)
public class MaEssentials {
    public static final String MODID = "maessentials";
    public static final IProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);


    public MaEssentials() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();

        modEventBus.addListener(this::onCommonSetup);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC, MODID + ".toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        PROXY.init();
        Networking.setup();
        DataLoader.setupMain(event);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        DataLoader.setupWorld(event);
        DataLoader.load();
    }

    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event) {
        ModCommands.registerCommands(event);
    }

}
