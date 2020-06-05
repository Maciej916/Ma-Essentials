package com.maciej916.maessentials.common.register;

import com.maciej916.maessentials.client.container.impl.EndcContainer;
import com.maciej916.maessentials.client.container.impl.InvseeContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModContainers {

    public static ContainerType<InvseeContainer> INVSEE;
    public static ContainerType<EndcContainer> ENDC;

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        INVSEE = registerContainer("invsee", IForgeContainerType.create(InvseeContainer::new));
        ENDC = registerContainer("endc", IForgeContainerType.create(EndcContainer::new));
    }

    private static <T extends Container> ContainerType<T> registerContainer(String name, ContainerType<T> container) {
        container.setRegistryName(name);
        ForgeRegistries.CONTAINERS.register(container);
        return container;
    }
    
}
