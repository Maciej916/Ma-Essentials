package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
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

public class HeadCommand extends BaseCommand {

    public HeadCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder
                .then(Commands.argument("targetPlayer", StringArgumentType.word()).executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "targetPlayer")))
                        .then(Commands.argument("amount", IntegerArgumentType.integer()).executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "targetPlayer"), IntegerArgumentType.getInteger(context, "amount")))));
    }

    private int execute(CommandSource source, String targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doHead(player, targetPlayer, 1);
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, String targetPlayer, int amount) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doHead(player, targetPlayer, amount);
        return Command.SINGLE_SUCCESS;
    }

    private void doHead(ServerPlayerEntity player, String targetPlayer, int amount) {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD, amount);
        CompoundNBT compound = stack.getOrCreateTag();
        compound.putString("SkullOwner", targetPlayer);
        sendMessage(player, "head.maessentials.done", targetPlayer);
        player.inventory.addItemStackToInventory(stack);
        player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
    }

}
