package com.cicdez.modelsimpler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class BlockFileManager extends FileManager {
    public static void startBlockModel(String folder, String id, BlockType type) throws IOException {
        File modId = getModIdFolder(folder);
        File blockOrBlocks = blockOrBlocks(modId);
        createBlockModel(modId, id, blockOrBlocks, type);
        ItemFileManager.createBlockItemModel(modId, id);
        createBlockState(modId, id, type);
    }

    public static void createBlockModel(File modId, String id, File blockOrBlocks, BlockType type)
            throws IOException {
        if (id.isEmpty()) throw new IOException("Id is Empty");
        File modelsDir = new File(modId, "models/block");
        modelsDir.mkdirs();
        RandomAccessFile model = new RandomAccessFile(new File(modelsDir, id + ".json"), "rw");
        type.writeToFile(model, Main.createBlockModelMap(modId.getName(), id, blockOrBlocks.getName()));
    }

    public static File blockOrBlocks(File modId) {
        File block = new File(modId, "textures/block");
        File blocks = new File(modId, "textures/blocks");

        if (block.exists() && blocks.exists()) {
            return block;
        } else if (block.exists() && !blocks.exists()) {
            return block;
        } else if (!block.exists() && blocks.exists()) {
            return blocks;
        } else if (!block.exists() && !blocks.exists()) {
            block.mkdirs();
            return block;
        } else return block;
    }

    public static void startColumnModel(String folder, String id, boolean includesHorizontal)
            throws IOException {
        File modId = getModIdFolder(folder);
        File blockOrBlocks = blockOrBlocks(modId);
        createBlockModel(modId, id, blockOrBlocks, BlockType.COLUMN);
        if (includesHorizontal) createHorizontalColumnModel(modId, id, blockOrBlocks);
        ItemFileManager.createBlockItemModel(modId, id);
        createBlockState(modId, id, BlockType.COLUMN);
    }
    private static void createHorizontalColumnModel(File modId, String id, File blockOrBlocks)
            throws IOException {
        if (id.isEmpty()) throw new IOException("Id is Empty");
        File modelsDir = new File(modId, "models/block");
        modelsDir.mkdirs();
        RandomAccessFile model = new RandomAccessFile(new File(modelsDir,
                id + "_horizontal.json"), "rw");
        BlockType.COLUMN_HORIZONTAL.writeToFile(model,
                Main.createBlockModelMap(modId.getName(), id, blockOrBlocks.getName()));
    }

    public static void startOrientedModel(String folder, String id, boolean includesVertical)
            throws IOException {
        File modId = getModIdFolder(folder);
        File blockOrBlocks = blockOrBlocks(modId);
        createBlockModel(modId, id, blockOrBlocks, BlockType.ORIENTABLE_BOTTOM);
        if (includesVertical) createVerticalOrientedModel(modId, id, blockOrBlocks);
        ItemFileManager.createBlockItemModel(modId, id);
        createBlockState(modId, id, BlockType.ORIENTABLE_BOTTOM);
    }
    private static void createVerticalOrientedModel(File modId, String id, File blockOrBlocks)
            throws IOException {
        if (id.isEmpty()) throw new IOException("Id is Empty");
        File modelsDir = new File(modId, "models/block");
        modelsDir.mkdirs();
        RandomAccessFile model = new RandomAccessFile(new File(modelsDir,
                id + "_vertical.json"), "rw");
        BlockType.ORIENTABLE_VERTICAL.writeToFile(model,
                Main.createBlockModelMap(modId.getName(), id, blockOrBlocks.getName()));
    }

    public static void startCropModel(String folder, String id, int stages) throws IOException {
        CropFileManager.startCropModel(folder, id, stages);
    }


    public static void createBlockState(File modId, String id, BlockType type) throws IOException {
        File blockStatesDir = new File(modId, "blockstates");
        blockStatesDir.mkdirs();
        RandomAccessFile state = new RandomAccessFile(new File(blockStatesDir,
                id + ".json"), "rw");
        String jsonState = type.getStateSource();
        String formattedJsonState = Main.formatWithMap(jsonState,
                Main.createBlockStateMap(modId.getName(), id));
        state.write(formattedJsonState.getBytes(StandardCharsets.UTF_8));
    }

    private static class CropFileManager extends FileManager {
        private static void startCropModel(String folder, String id, int stages) throws IOException {
            File modId = getModIdFolder(folder);
            File blockOrBlocks = blockOrBlocks(modId);
            for (int stage = 0; stage < stages; stage++) {
                createCropModel(modId, id, blockOrBlocks, stage);
            }
            createCropBlockState(modId, id, stages);
        }

        private static void createCropModel(File modId, String id, File blockOrBlocks, int stage)
                throws IOException {
            if (id.isEmpty()) throw new IOException("Id is Empty");
            File modelsDir = new File(modId, "models/block");
            modelsDir.mkdirs();
            RandomAccessFile model = new RandomAccessFile(new File(modelsDir,
                    id + stage + ".json"), "rw");
            Map<String, Object> map = Main.createBlockModelMap(modId.getName(), id, blockOrBlocks.getName());
            map.put("stage", stage);
            BlockType.CROP.writeToFile(model, map);
        }

        private static void createCropBlockState(File modId, String id, int stages) throws IOException {
            File blockStatesDir = new File(modId, "blockstates");
            blockStatesDir.mkdirs();
            RandomAccessFile state = new RandomAccessFile(new File(blockStatesDir,
                    id + ".json"), "rw");
            String jsonState = createBlockStateContent(modId.getName(), id, stages);
            state.write(jsonState.getBytes(StandardCharsets.UTF_8));
        }

        private static String createBlockStateContent(String modId, String id, int stages) {
            String defaultCrop = Main.readStream("/resources/block/state/crop.json");
            String[] cropLines = defaultCrop.split("\n");

            StringBuilder crop = new StringBuilder();

            crop.append(cropLines[0]).append("\n").append(cropLines[1]).append("\n");

            String l2 = cropLines[2], l3 = cropLines[3], l4 = cropLines[4];

            for (int stage = 0; stage < stages; stage++) {
                Map<String, Object> map = Main.createBlockStateMap(modId, id);
                map.put("stage", stage);

                String fl2 = Main.formatWithMap(l2, map), fl3 = Main.formatWithMap(l3, map);

                crop.append(fl2).append("\n").append(fl3).append("\n").append(l4);
                if (stage != stages - 1) crop.append(",");
                crop.append("\n");
            }

            crop.append(cropLines[5]).append("\n").append(cropLines[6]).append("\n");

            return crop.toString();
        }
    }
}
