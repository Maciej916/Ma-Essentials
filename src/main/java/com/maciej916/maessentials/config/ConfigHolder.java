package com.maciej916.maessentials.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigHolder {

	public static final ForgeConfigSpec COMMON_SPEC;
	static final CommonConfig COMMON;
	static {
		{
			final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
			COMMON = specPair.getLeft();
			COMMON_SPEC = specPair.getRight();
		}
	}
}
