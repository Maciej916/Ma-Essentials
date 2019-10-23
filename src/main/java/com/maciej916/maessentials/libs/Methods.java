package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.classes.KitItem;
import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.KitsData;
import com.maciej916.maessentials.data.PlayerData;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import javax.xml.crypto.Data;
import java.util.Optional;

import static com.maciej916.maessentials.MaEssentials.MODID;

public class Methods {

    public static final SuggestionProvider<CommandSource> HOME_SUGGEST = (context, builder) -> ISuggestionProvider.suggest(DataManager.getPlayerData(context.getSource().asPlayer()).getHomes().keySet().stream().toArray(String[]::new), builder);

    public static final SuggestionProvider<CommandSource> WARP_SUGGEST = (context, builder) -> ISuggestionProvider.suggest(DataManager.getWarpData().getWarps().keySet().stream().toArray(String[]::new), builder);

    public static final SuggestionProvider<CommandSource> KIT_SUGGEST = (context, builder) -> ISuggestionProvider.suggest(DataManager.getKitsData().getKits().keySet().stream().toArray(String[]::new), builder);

    private static String getVersion() {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById(MODID);
        if (o.isPresent()) {
            return o.get().getModInfo().getVersion().toString();
        }
        return "NONE";
    }

    public static boolean isDev() {
        String version = getVersion();
        return version.equals("NONE");
    }

    public static TextComponent formatText(String translationKey, TextFormatting color, Object... args) {
        TextComponent msg;
        if (args != null) {
            msg = new TranslationTextComponent(translationKey, args);
        } else {
            msg = new TranslationTextComponent(translationKey);
        }
        msg.getStyle().setColor(color);
        return msg;
    }

    public static boolean isLocationSame(Location fistLocation, Location secoondLocation) {
        if (fistLocation.x == secoondLocation.x && fistLocation.y == secoondLocation.y && fistLocation.z == secoondLocation.z && fistLocation.dimension == secoondLocation.dimension ) {
            return true;
        }
        return false;
    }

    public static long delayCommand(long time, int cooldown) {
        long currentTime = System.currentTimeMillis() / 1000;
        if (cooldown == 0 || time + cooldown < currentTime) {
            return 0;
        } else {
            long timeleft = time + cooldown - currentTime;
            return timeleft;
        }
    }

    public static void giveKit(ServerPlayerEntity player, String kitName) {
        PlayerData playerData = DataManager.getPlayerData(player);
        KitsData kit = DataManager.getKitsData().getKit(kitName);
        try {
            for (KitItem item : kit.getItems()) {
                ItemInput itemInput = item.getItemInput();
                if (itemInput != null) {
                    ItemStack itemstack = item.getItemInput().createStack(item.getQuantity(), true);
                    player.inventory.addItemStackToInventory(itemstack);
                }
            }
        } catch (Exception e) {
            Log.err("Failed to parse item for kit: "+ kitName);
            player.sendMessage(Methods.formatText("kit.maessentials.parse_error", TextFormatting.RED));
            System.out.println(e);
        }

        long currentTime = System.currentTimeMillis() / 1000;
        playerData.setKitUsage(kitName, currentTime);
        DataManager.savePlayerData(playerData);
    }


}

