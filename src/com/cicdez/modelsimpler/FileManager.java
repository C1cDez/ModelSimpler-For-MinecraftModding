package com.cicdez.modelsimpler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class FileManager {
    protected FileManager() {}

    public static File getModIdFolder(String folder) throws IOException {
        File assets = getAssetsFolder(folder);
        if (assets == null) return new File(folder);

        File[] namespaces = assets.listFiles();
        if (namespaces != null) {
            if (namespaces.length == 0) throw new IOException("Not found Namespaces");
            if (namespaces.length == 1) {
                if (!namespaces[0].getName().equals("minecraft")) return namespaces[0];
                else throw new IOException("Found only Minecraft Dir");
            }
            if (namespaces.length == 2) {
                if (namespaces[0].getName().equals("minecraft")) return namespaces[1];
                if (namespaces[1].getName().equals("minecraft")) return namespaces[0];
            } else {
                for (File namespace : namespaces) {
                    if (!namespace.getName().equals("minecraft")) return namespace;
                }
            }
        }
        return null;
    }

    public static File getAssetsFolder(String folder) throws IOException {
        if (folder.isEmpty()) throw new IOException("Folder is Empty!");
        File dir = new File(folder);
        if (!dir.exists()) throw new IOException("Not found Directory");
        String dirName = dir.getName();
        switch (dirName) {
            case "src":
                return new File(dir, "main/resources/assets");
            case "main":
                return new File(dir, "resources/assets");
            case "resources":
                return new File(dir, "assets");
            case "assets":
                return dir;
            default: {
                if (dir.getParentFile().getName().equals("assets")) return null;
                else return new File(dir, "src/main/resources/assets");
            }
        }
    }
}
