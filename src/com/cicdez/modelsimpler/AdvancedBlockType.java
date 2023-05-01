package com.cicdez.modelsimpler;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;
import java.util.Map;

public enum AdvancedBlockType implements IModelType {
    SLAB(true, "", "top"),
    STAIRS(true, "", "outer", "inner"),
    DOOR(false, "bottom", "bottom_hinge", "top", "top_hinge"),
    TRAPDOOR(true, "bottom", "open", "top"),
    FENCE(true, "inventory", "post", "side"),
    FENCE_GATE(true, "", "open", "wall", "wall_open"),
    PRESSURE_PLATE(true, "up", "down"),
    BUTTON(true, "", "inventory", "pressed"),
    WALL(true, "inventory", "post", "side", "side_tall")
    ;

    private final String id;
    private final String[] variants;
    private final boolean hasItemModel;
    private final String itemModelPrefix;

    AdvancedBlockType(boolean hasItemModel, String... variants) {
        this.id = name().toLowerCase(Locale.ROOT);
        this.variants = variants;
        this.hasItemModel = hasItemModel;
        this.itemModelPrefix = hasItemModel ? variants[0] : null;
    }

    public String getVariant(int index) {
        return variants[index];
    }
    public String[] getVariants() {
        return variants;
    }

    public int variantsCount() {
        return variants.length;
    }

    public String applyVariant(int index) {
        String variant = getVariant(index);
        return variant.isEmpty() ? "" : ("_" + variant);
    }

    public String getItemModelPrefix() {
        return hasItemModel ? (itemModelPrefix.isEmpty() ? "" : ("_" + itemModelPrefix)) : "";
    }
    public boolean isHasItemModel() {
        return hasItemModel;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSource() {
        return Main.readStream("/resources/advanced_block/" + id + ".json");
    }

    public String getStateSource() {
        return Main.readStream("/resources/advanced_block/state/" + id + ".json");
    }

    @Override
    public void writeToFile(RandomAccessFile file, Map<String, Object> map) throws IOException {
        throw new IOException("I can't do this");
    }

    public static String[] allDisplayNames() {
        AdvancedBlockType[] types = values();
        String[] names = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            names[i] = types[i].getDisplayName();
        }
        return names;
    }

    public static AdvancedBlockType byId(int index) {
        return values()[index];
    }
}
