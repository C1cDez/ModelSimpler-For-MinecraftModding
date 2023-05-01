package com.cicdez.modelsimpler;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;
import java.util.Map;

public enum BlockType implements IModelType {
    CUBE("default"),
    CUBE_ALL("default"),
    COLUMN("column"),
    COLUMN_HORIZONTAL("column"),
    CROSS("default"),
    TINTED_CROSS("default"),
    BOTTOM_TOP("default"),
    TOP("default"),
    CARPET("default"),
    ORIENTABLE_BOTTOM("faced_oriented"),
    ORIENTABLE_VERTICAL("faced_oriented"),
    CROP("crop")
    ;

    private final String id;
    private final String stateId;
    BlockType(String stateId) {
        this.id = name().toLowerCase(Locale.ROOT);
        this.stateId = stateId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSource() {
        return Main.readStream("/resources/block/" + id + ".json");
    }

    public String getStateSource() {
        return Main.readStream("/resources/block/state/" + stateId + ".json");
    }

    public static String[] allDisplayNames() {
        BlockType[] types = values();
        String[] names = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            names[i] = types[i].getDisplayName();
        }
        return names;
    }

    public static BlockType byId(int index) {
        return values()[index];
    }
}
