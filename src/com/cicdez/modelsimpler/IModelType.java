package com.cicdez.modelsimpler;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

public interface IModelType {
    String getId();

    default String getDisplayName() {
        String[] words = getId().split("_");
        String[] newWords = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            String oldWord = words[i];
            newWords[i] = String.valueOf(oldWord.charAt(0)).toUpperCase(Locale.ROOT)
                    + oldWord.substring(1);
        }
        StringJoiner joiner = new StringJoiner(" ");
        for (String word : newWords) {
            joiner.add(word);
        }
        return joiner.toString();
    }

    String getSource();

    default void writeToFile(RandomAccessFile file, Map<String, Object> map) throws IOException {
        String source = getSource();
        String format = Main.formatWithMap(source, map);
        file.write(format.getBytes(StandardCharsets.UTF_8));
    }
}
