package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;

public class GmCommand extends BaseCommand {

    public GmCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    private static final String[] gm = {"0", "1", "2", "3"};
    private static final SuggestionProvider<CommandSource> GM_SUGGEST = (context, builder) -> ISuggestionProvider.suggest(gm, builder);

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.then(Commands.argument("gamemode", IntegerArgumentType.integer()).suggests(GM_SUGGEST).executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "gamemode"))).then(Commands.argument("targetPlayer", EntityArgument.players()).executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "gamemode"), EntityArgument.getPlayer(context, "targetPlayer")))));
    }

    private int execute(CommandSource source, int gm) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doGm(player, player, gm);
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, int gm, ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doGm(player, targetPlayer, gm);
        return Command.SINGLE_SUCCESS;
    }

    private void doGm(ServerPlayerEntity player, ServerPlayerEntity targetPlayer, int gm) {
        boolean changed = false;
        switch (gm) {
            case 0:
                if (targetPlayer.interactionManager.getGameType() != GameType.SURVIVAL) {
                    targetPlayer.setGameType(GameType.SURVIVAL);
                    changed = true;
                }
                break;
            case 1:
                if (targetPlayer.interactionManager.getGameType() != GameType.CREATIVE) {
                    targetPlayer.setGameType(GameType.CREATIVE);
                    changed = true;
                }
                break;
            case 2:
                if (targetPlayer.interactionManager.getGameType() != GameType.ADVENTURE) {
                    targetPlayer.setGameType(GameType.ADVENTURE);
                    changed = true;
                }
                break;
            case 3:
                if (targetPlayer.interactionManager.getGameType() != GameType.SPECTATOR) {
                    targetPlayer.setGameType(GameType.SPECTATOR);
                    changed = true;
                }
                break;
            default : sendMessage(player, "gm.maessentials.invalid");
        }

        if (changed) {
            ITextComponent newGm = targetPlayer.interactionManager.getGameType().getDisplayName();
            if (player == targetPlayer) {
                sendMessage(player, "gm.maessentials.self", newGm);
            } else {
                sendMessage(player, "gm.maessentials.player", targetPlayer.getDisplayName(), newGm);
                sendMessage(targetPlayer, "gm.maessentials.self", newGm);
            }
        }
    }

}
