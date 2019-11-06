package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.kit.Kit;
import com.maciej916.maessentials.data.DataManager;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Optional;

import static com.maciej916.maessentials.MaEssentials.MODID;

public class Methods {

    public static final SuggestionProvider<CommandSource> HOME_SUGGEST = (context, builder) -> ISuggestionProvider.suggest(DataManager.getPlayer(context.getSource().asPlayer()).getHomeData().getHomes().keySet().stream().toArray(String[]::new), builder);

    public static final SuggestionProvider<CommandSource> WARP_SUGGEST = (context, builder) -> ISuggestionProvider.suggest(DataManager.getWarp().getWarps().keySet().stream().toArray(String[]::new), builder);

    public static final SuggestionProvider<CommandSource> KIT_SUGGEST = (context, builder) -> ISuggestionProvider.suggest(DataManager.getKit().getKits().keySet().stream().toArray(String[]::new), builder);

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

    public static ArrayList<String> catalogFiles(String catalog) {
        File folder = new File(catalog);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> data = new ArrayList<>();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String name = FilenameUtils.removeExtension(listOfFiles[i].getName());
                    data.add(name);
                }
            }
        }
        return data;
    }

    public static FileReader loadFile(String catalog, String fileName) throws Exception {
        return new FileReader(catalog + fileName + ".json");
    }

    public static boolean fileExist(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static long currentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static boolean giveKit(ServerPlayerEntity player, Kit kit) {
        if (kit.getItems() == null) {
            player.sendMessage(Methods.formatText("kit.maessentials.parse_error"));
            return false;
        }

        for (ItemStack item : kit.getItems()) {
            player.inventory.addItemStackToInventory(item);
        }
        return true;
    }









    public static TextComponent formatText(String translationKey, Object... args) {
        TextComponent msg = new TranslationTextComponent(translationKey, args);
        return msg;
    }

    public static boolean isLocationSame(Location fistLocation, Location secoondLocation) {
        if (fistLocation.x == secoondLocation.x && fistLocation.y == secoondLocation.y && fistLocation.z == secoondLocation.z && fistLocation.dimension == secoondLocation.dimension) {
            return true;
        }
        return false;
    }
}

