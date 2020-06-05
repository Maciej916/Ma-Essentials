package com.maciej916.maessentials.client.subscribe;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.client.gui.impl.EndcGui;
import com.maciej916.maessentials.client.gui.impl.InvseeGui;
import com.maciej916.maessentials.common.register.ModContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = MaEssentials.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEventSubscriber {

	@SubscribeEvent
	public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
		ScreenManager.registerFactory(ModContainers.INVSEE, InvseeGui::new);
		ScreenManager.registerFactory(ModContainers.ENDC, EndcGui::new);
	}

}
