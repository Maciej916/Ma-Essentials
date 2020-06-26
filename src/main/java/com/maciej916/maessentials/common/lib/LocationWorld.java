package com.maciej916.maessentials.common.lib;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class LocationWorld {

    protected String dimension_provider;
    protected String dimension_location;

    public LocationWorld(RegistryKey<World> worldRegistryKey) {

    }

    public LocationWorld(String dimension_provider, String dimension_location) {
        this.dimension_provider = dimension_provider;
        this.dimension_location = dimension_location;
    }

    public ResourceLocation getDimensionProviderResource() {
        return new ResourceLocation(dimension_provider);
    }

    public ResourceLocation getDimensionLocationResource() {
        return new ResourceLocation(dimension_location);
    }
}
