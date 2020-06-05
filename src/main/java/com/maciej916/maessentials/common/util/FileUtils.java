package com.maciej916.maessentials.common.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public final class FileUtils {

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
        return file.exists();
    }

}
