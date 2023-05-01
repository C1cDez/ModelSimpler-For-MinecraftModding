package com.cicdez.modelsimpler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class AdvancedBlockFileManager extends FileManager {
    public static void startAdvancedBlockModel(String folder, String id, AdvancedBlockType type,
                                               boolean increaseId, boolean useCustomTexture)
            throws IOException {
        File modId = getModIdFolder(folder);
        File blockOrBlocks = BlockFileManager.blockOrBlocks(modId);
        String fullId = increaseId ? getFullId(id, type) : id;
        String textureId = useCustomTexture ? fullId : id;

        createBlockModels(modId, fullId, textureId, blockOrBlocks, type);
        if (type.isHasItemModel()) createBlockItemModel(modId, fullId, type);
        createBlockState(modId, id, fullId, type);
    }

    private static String getFullId(String rawId, AdvancedBlockType type) {
        return rawId.endsWith(type.getId()) ? rawId : (rawId + "_" + type.getId());
    }

    public static void createBlockModels(File modId, String blockId, String textureId,
                                         File blockOrBlocks, AdvancedBlockType type)
            throws IOException {
        if (blockId.isEmpty() || textureId.isEmpty()) throw new IOException("Id is Empty");
        File modelsDir = new File(modId, "models/block");
        modelsDir.mkdirs();
        for (int i = 0; i < type.variantsCount(); i++) {
            String variant = type.applyVariant(i);
            File modelFile = new File(modelsDir, blockId + variant + ".json");
            RandomAccessFile model = new RandomAccessFile(modelFile, "rw");
            Map<String, Object> map = Main.createBlockModelMap(modId.getName(),
                    textureId, blockOrBlocks.getName());
            map.put("variant", variant);
            String jsonModel = Main.formatWithMap(type.getSource(), map);
            model.write(jsonModel.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void startFullComplex(String folder, String id, boolean useCustomTexture)
            throws IOException {
        for (AdvancedBlockType type : AdvancedBlockType.values()) {
            startAdvancedBlockModel(folder, id, type, true, useCustomTexture);
        }
    }

    public static void createBlockState(File modId, String id, String fullId, AdvancedBlockType type)
            throws IOException {
        File blockStatesDir = new File(modId, "blockstates");
        blockStatesDir.mkdirs();
        RandomAccessFile state = new RandomAccessFile(new File(blockStatesDir,
                fullId + ".json"), "rw");
        Map<String, Object> map = Main.createBlockStateMap(modId.getName(), fullId);
        map.put("idStart", id);
        String jsonState = Main.formatWithMap(type.getStateSource(), map);
        state.write(jsonState.getBytes(StandardCharsets.UTF_8));
    }

    public static void createBlockItemModel(File modId, String id, AdvancedBlockType type)
            throws IOException {
        if (id.isEmpty()) throw new IOException("Id is Empty!");
        File modelsDir = new File(modId, "models/item");
        modelsDir.mkdirs();
        RandomAccessFile model = new RandomAccessFile(new File(modelsDir, id + ".json"), "rw");
        String context = Main.createBlockItemModelContext(modId.getName(),
                id + type.getItemModelPrefix());
        model.write(context.getBytes(StandardCharsets.UTF_8));
    }
}
