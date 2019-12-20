package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;

public class CommandSuicide {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        if (ConfigValues.suicide_enable_player) {
            LiteralArgumentBuilder<CommandSource> builder = Commands.literal("suicide").requires(source -> source.hasPermissionLevel(0));
            builder
                    .executes(context -> suicide(context));
            dispatcher.register(builder);
        }

        LiteralArgumentBuilder<CommandSource> builder2 = Commands.literal("suicide").requires(source -> source.hasPermissionLevel(2));
        builder2
                .then(Commands.argument("targetPlayer", EntityArgument.players())
                        .executes(context -> suicideArgs(context)));
        dispatcher.register(builder2);
    }

    private static int suicide(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        long cooldown = eslPlayer.getUsage().getCommandCooldown("suicide", ConfigValues.suicide_player_cooldown);
        if (cooldown != 0) {
            player.sendMessage(Methods.formatText("maessentials.cooldown", cooldown));
            return Command.SINGLE_SUCCESS;
        }

        eslPlayer.getUsage().setCommandUsage("suicide");
        eslPlayer.saveData();

        doSuicide(player, player);
        return Command.SINGLE_SUCCESS;
    }

    private static int suicideArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity targetPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        doSuicide(player, targetPlayer);
        return Command.SINGLE_SUCCESS;
    }

    private static void doSuicide(ServerPlayerEntity player, ServerPlayerEntity target) {
        if (target.interactionManager.getGameType() == GameType.CREATIVE || target.interactionManager.getGameType() == GameType.SPECTATOR) {
            if (player == target) {
                target.sendMessage(Methods.formatText("maessentials.invaild_gamemode"));
            } else {
                target.sendMessage(Methods.formatText("maessentials.invaild_gamemode.player", target.getDisplayName()));
            }
            return;
        }

        target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, target.getHealth());

        ServerWorld world = (ServerWorld) player.world;
        LightningBoltEntity entity = new LightningBoltEntity(world, player.func_226277_ct_(), player.func_226278_cu_(), player.func_226281_cx_(), true);
        world.addLightningBolt(entity);

        if (player != target) {
            player.sendMessage(Methods.formatText("suicide.maessentials.player", target.getDisplayName()));
            target.sendMessage(Methods.formatText("suicide.maessentials.player.target", player.getDisplayName()));
        }
    }
}