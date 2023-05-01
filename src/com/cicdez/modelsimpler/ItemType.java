package com.cicdez.modelsimpler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum ItemType implements IModelType {
    GENERATED,
    HANDHELD,
    HANDHELD_ROD
    ;

    private final String id;
    ItemType() {
        this.id = name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getId() {
        return id;
    }
    @Override
    public String getSource() {
        return Main.readStream("/resources/item/" + id + ".json");
    }

    public static String[] allDisplayNames() {
        ItemType[] types = values();
        String[] names = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            names[i] = types[i].getDisplayName();
        }
        return names;
    }

    public static ItemType byId(int index) {
        return values()[index];
    }
}
