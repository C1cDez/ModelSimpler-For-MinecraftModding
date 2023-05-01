package com.cicdez.modelsimpler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public final class ItemFileManager extends FileManager {
    public static void startItemModel(String folder, String id, ItemType type) throws IOException {
        File modId = getModIdFolder(folder);
        File itemOrItems = itemOrItems(modId);
        createItemModel(modId, id, itemOrItems, type);
    }

    public static void createItemModel(File modId, String id, File itemOrItems, ItemType type)
            throws IOException {
        if (id.isEmpty()) throw new IOException("Id is Empty");
        File modelsDir = new File(modId, "models/item");
        modelsDir.mkdirs();
        RandomAccessFile model = new RandomAccessFile(new File(modelsDir, id + ".json"), "rw");
        type.writeToFile(model, Main.createItemModelMap(modId.getName(), id, itemOrItems.getName()));
    }

    public static void createBlockItemModel(File modId, String id) throws IOException {
        if (id.isEmpty()) throw new IOException("Id is Empty");
        File modelsDir = new File(modId, "models/item");
        modelsDir.mkdirs();
        RandomAccessFile model = new RandomAccessFile(new File(modelsDir, id + ".json"), "rw");
        model.write(Main.createBlockItemModelContext(modId.getName(), id).getBytes(StandardCharsets.UTF_8));
    }

    public static File itemOrItems(File modId) {
        File item = new File(modId, "textures/item");
        File items = new File(modId, "textures/items");

        if (item.exists() && items.exists()) {
            return item;
        } else if (item.exists() && !items.exists()) {
            return item;
        } else if (!item.exists() && items.exists()) {
            return items;
        } else if (!item.exists() && !items.exists()) {
            item.mkdirs();
            return item;
        } else return item;
    }
}
