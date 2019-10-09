package com.maciej916.maessentials.data;

import com.maciej916.maessentials.libs.PlayerHomes;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.HashMap;
import java.util.UUID;

public class HomeData {
    private static HashMap<UUID, PlayerHomes> playerHomes = new HashMap<>();

    public static final SuggestionProvider<CommandSource> HOME_SUGGEST = (context, builder) -> {
        ServerPlayerEntity player = context.getSource().asPlayer();
        return ISuggestionProvider.suggest(getPlayerHomes(player).getHomeNames().stream().toArray(String[]::new), builder);
    };

    public static void addPlayerHomes(UUID uuid, PlayerHomes homes) {
        playerHomes.put(uuid, homes);
    }

    public static PlayerHomes getPlayerHomes(ServerPlayerEntity player) {
        UUID playerUUID = player.getUniqueID();
        if (playerHomes.containsKey(playerUUID)) {
            return playerHomes.get(playerUUID);
        } else {
            PlayerHomes playerHome = new PlayerHomes();
            playerHomes.put(playerUUID, playerHome);
            return playerHome;
        }
    }
}
