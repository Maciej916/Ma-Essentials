package com.maciej916.maessentials.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;

public class CommandGm {
    private static final SuggestionProvider<CommandSource> GM_SUGGEST = (context, builder) -> {
        String[] gm = {"0", "1", "2", "3"};
        return ISuggestionProvider.suggest(gm, builder);
    };

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("gm").requires(source -> source.hasPermissionLevel(2));
        builder
                .executes(context -> gm(context))
                .then(Commands.argument("gamemode", StringArgumentType.word())
                        .suggests(GM_SUGGEST)
                        .executes(context -> gmSelf(context))
                            .then(Commands.argument("targetPlayer", EntityArgument.players())
                                    .executes(context -> gmOthers(context))));

        dispatcher.register(builder);
    }

    private static int gm(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(new TranslationTextComponent("command.maessentials.player.provide"));
        return Command.SINGLE_SUCCESS;
    }

    private static int gmSelf(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String args = StringArgumentType.getString(context, "gamemode").toString().toLowerCase();
        gmManage(player, player, args);
        return Command.SINGLE_SUCCESS;
    }

    private static int gmOthers(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        String args = StringArgumentType.getString(context, "gamemode").toString().toLowerCase();
        gmManage(player, requestedPlayer, args);
        return Command.SINGLE_SUCCESS;
    }

    private static void gmManage(ServerPlayerEntity player, ServerPlayerEntity targetPlayer, String gm) {
        String newGm = null;
        switch (gm) {
            case "0":
                if (targetPlayer.interactionManager.getGameType() != GameType.SURVIVAL) {
                    targetPlayer.setGameType(GameType.SURVIVAL);
                    newGm = "survival";
                }
                break;
            case "1":
                if (targetPlayer.interactionManager.getGameType() != GameType.CREATIVE) {
                    targetPlayer.setGameType(GameType.CREATIVE);
                    newGm = "creative";
                }
                break;
            case "2":
                if (targetPlayer.interactionManager.getGameType() != GameType.ADVENTURE) {
                    targetPlayer.setGameType(GameType.ADVENTURE);
                    newGm = "adventure";
                }
                break;
            case "3":
                if (targetPlayer.interactionManager.getGameType() != GameType.SPECTATOR) {
                    targetPlayer.setGameType(GameType.SPECTATOR);
                    newGm = "spectator";
                }
                break;
            default :
                player.sendMessage(new TranslationTextComponent("command.maessentials.gm.invalid"));
        }

        if (newGm != null) {
            if (player == targetPlayer) {
                player.sendMessage(new TranslationTextComponent("command.maessentials.gm.set.self", newGm, true));
            } else {
                player.sendMessage(new TranslationTextComponent("command.maessentials.gm.set.other", targetPlayer.getDisplayName(), newGm, true));
                targetPlayer.sendMessage(new TranslationTextComponent("command.maessentials.gm.set.self", newGm, true));
            }
        }
    }
}
