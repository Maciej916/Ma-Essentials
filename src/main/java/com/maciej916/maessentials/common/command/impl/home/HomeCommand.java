package com.maciej916.maessentials.common.command.impl.home;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.enums.EnumColor;
import com.maciej916.maessentials.common.enums.EnumLang;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.util.ModUtils;
import com.maciej916.maessentials.common.util.TextUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.maciej916.maessentials.common.util.TeleportUtils.simpleTeleport;

public class HomeCommand extends BaseCommand {

    public HomeCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(Commands.argument("homeName", StringArgumentType.string()).suggests(ModUtils.HOME_SUGGEST).executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "homeName"))));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doHome(player, "home");
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, String homeName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doHome(player, homeName);
        return Command.SINGLE_SUCCESS;
    }

    private void doHome(ServerPlayerEntity player, String name) {
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        if (eslPlayer.getHomeData().getHomes().size() == 0) {
            sendMessage(player, "home.maessentials.no_homes");
            return;
        }

        Location location = eslPlayer.getHomeData().getHome(name);
        if (location == null) {
            sendMessage(player, "home.maessentials.not_exist", name);
            return;
        }

        long cooldown = eslPlayer.getUsage().getTeleportCooldown("home", ModConfig.homes_cooldown);
        if (cooldown != 0) {
            TextUtils.sendChatMessage(player, EnumLang.TELEPORT_COOLDOWN.translateColored(EnumColor.DARK_RED, EnumLang.GENERIC.translateColored(EnumColor.RED, cooldown)));
            return;
        }

        eslPlayer.getUsage().setCommandUsage("home");
        eslPlayer.saveData();

        if (simpleTeleport(player, location, "home", ModConfig.homes_delay)) {
            if (ModConfig.homes_delay == 0) {
                sendMessage(player, "home.maessentials.teleport", name);
            } else {
                sendMessage(player, "home.maessentials.teleport.wait", name, ModConfig.homes_delay);
            }
        }
    }

}
