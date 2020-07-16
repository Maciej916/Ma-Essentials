package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.kit.Kit;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.util.ModUtils;
import com.maciej916.maessentials.common.util.TimeUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class AdminKitCommand extends BaseCommand {

    public AdminKitCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.then(Commands.argument("kitName", StringArgumentType.string()).suggests(ModUtils.KIT_SUGGEST).executes(context -> execute(context.getSource(), StringArgumentType.getString(context, "kitName"))));
    }

    private int execute(CommandSource source, String kitName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);



        return Command.SINGLE_SUCCESS;
    }

}
