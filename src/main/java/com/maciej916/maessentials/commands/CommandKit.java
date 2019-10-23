package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.KitsData;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Log;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Time;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TextFormatting;

import java.util.Set;

public class CommandKit {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kit").requires(source -> source.hasPermissionLevel(0));
        builder
            .executes(context -> kit(context))
            .then(Commands.argument("kitName", StringArgumentType.string())
                .suggests(Methods.KIT_SUGGEST)
                .executes(context -> kitArgs(context)));

        dispatcher.register(builder);
    }
    private static int kit(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        Set<String> kits =  DataManager.getKitsData().getKits().keySet();
        StringBuilder kitsString = new StringBuilder();
        if (kits.size() != 0) {
            int i = 1;
            for (String name : kits) {
                kitsString.append(name);
                if (kits.size() != i) {
                    kitsString.append(", ");
                    i++;
                }
            }
        } else {
            kitsString.append("-");
        }
        player.sendMessage(Methods.formatText("kit.maessentials.list", TextFormatting.WHITE, kitsString));
        return Command.SINGLE_SUCCESS;
    }

    private static int kitArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        PlayerData playerData = DataManager.getPlayerData(player);
        String kitName = StringArgumentType.getString(context, "kitName").toLowerCase();
        KitsData kit = DataManager.getKitsData().getKit(kitName);
        if (kit != null) {
            long currentTime = System.currentTimeMillis() / 1000;
            if (playerData.getKitUsage(kitName) == null || playerData.getKitUsage(kitName) + kit.getDuration() < currentTime) {
                Methods.giveKit(player, kitName);
                player.world.playSound((PlayerEntity)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.sendMessage(Methods.formatText("kit.maessentials.received", TextFormatting.WHITE, kitName));
            } else {
                String displayTime = Time.formatDate(playerData.getKitUsage(kitName) + kit.getDuration() - currentTime);
                player.sendMessage(Methods.formatText("kit.maessentials.wait", TextFormatting.RED, displayTime));
            }
        } else {
            player.sendMessage(Methods.formatText("kit.maessentials.not_exist", TextFormatting.RED, kitName));
        }
        return Command.SINGLE_SUCCESS;
    }
}
