package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class CommandHead {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("head").requires((source) -> source.hasPermissionLevel(2))
            .then(Commands.argument("targetPlayer", StringArgumentType.word()).executes((context) -> head(context.getSource(), StringArgumentType.getString(context, "targetPlayer")))
                .then(Commands.argument("amount", IntegerArgumentType.integer()).executes((context) -> head(context.getSource(), StringArgumentType.getString(context, "targetPlayer"), IntegerArgumentType.getInteger(context, "amount"))))
            )
        );
    }

    private static int head(CommandSource source, String targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        giveHead(player, targetPlayer, 1);
        return Command.SINGLE_SUCCESS;
    }

    private static int head(CommandSource source, String targetPlayer, int amount) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        giveHead(player, targetPlayer, amount);
        return Command.SINGLE_SUCCESS;
    }

    private static void giveHead(ServerPlayerEntity player, String targetPlayer, int amount) {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD, amount);
        CompoundNBT compound = stack.getOrCreateTag();
        compound.putString("SkullOwner", targetPlayer);

        player.sendMessage(Methods.formatText("head.maessentials.done", targetPlayer));
        player.inventory.addItemStackToInventory(stack);
        player.world.playSound((PlayerEntity)null, player.func_226277_ct_(), player.func_226278_cu_(), player.func_226281_cx_(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
    }

}