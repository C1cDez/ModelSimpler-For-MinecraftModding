package com.cicdez.modelsimpler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ModelSimplerFrame.init();
    }

    public static String formatWithMap(String msg, Map<String, Object> map) {
        if (msg == null) return "";
        if (map == null) return msg;

        if (map.size() != 0) {
            String str = "";
            for (int i = 0; i < msg.length(); i++) {
                if (msg.charAt(i) == '#') {
                    if (msg.charAt(i + 1) == '{') {
                        String id = "";
                        i += 2;
                        while (msg.charAt(i) != '}') {
                            id += msg.charAt(i);
                            i++;
                        }
                        Object obj = map.get(id);
                        str += obj == null ? "" : obj.toString();
                    } else str += msg.charAt(i);
                } else str += msg.charAt(i);
            }
            return str;
        } else return msg;
    }

    public static String formatWithArgs(String msg, Object... args) {
        if (args.length != 0) {
            String str = "";
            for (int i = 0; i < msg.length(); i++) {
                if (msg.charAt(i) == '#') {
                    if (msg.charAt(i + 1) == '[') {
                        String tIndex = "";
                        i += 2;
                        while (msg.charAt(i) != ']') {
                            tIndex += msg.charAt(i);
                            i++;
                        }
                        int index = Integer.parseInt(tIndex);
                        Object obj = args[index];
                        str += obj == null ? "" : obj.toString();
                    } else str += msg.charAt(i);
                } else str += msg.charAt(i);
            }
            return str;
        } else return msg;
    }

    public static InputStream loadStream(String pathname) {
        return Main.class.getResourceAsStream(pathname.startsWith("/") ? pathname : ("/" + pathname));
    }
    public static String readStream(String pathname) {
        InputStream stream = loadStream(pathname);
        if (stream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder text = new StringBuilder();
            reader.lines().forEach(line -> text.append(line).append("\n"));
            return text.toString();
        }
        return "Not Found Resource '" + pathname + "'";
    }
    public static String[] getFiles(String pathname) {
        InputStream stream = loadStream(pathname + "/");
        if (stream != null) {
            List<String> files = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            reader.lines().forEach(files::add);
            return files.toArray(new String[0]);
        }
        return null;
    }

    public static Map<String, Object> createItemModelMap(String modId, String id, String itemOrItems) {
        Map<String, Object> map = new HashMap<>();
        map.put("modId", modId);
        map.put("id", id);
        map.put("itemOrItems", itemOrItems);
        return map;
    }
    public static Map<String, Object> createBlockModelMap(String modId, String id, String blockOrBlocks) {
        Map<String, Object> map = new HashMap<>();
        map.put("modId", modId);
        map.put("id", id);
        map.put("blockOrBlocks", blockOrBlocks);
        return map;
    }
    public static String createBlockItemModelContext(String modId, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("modId", modId);
        map.put("id", id);

        String text = readStream("/resources/item/block_item.json");
        return formatWithMap(text, map);
    }
    public static Map<String, Object> createBlockStateMap(String modId, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("modId", modId);
        map.put("id", id);
        return map;
    }
}
