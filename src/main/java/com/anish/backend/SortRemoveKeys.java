package com.anish.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SortRemoveKeys {
    public static void main(String[] args) {
        SortRemoveKeys obj = new SortRemoveKeys();
        Set<String> keysToRemove = obj.readKeysFromFile("remove_keys.txt");
        obj.writeKeysToFile(keysToRemove);
    }

    private Set<String> readKeysFromFile(String filePath) {
        SortedSet<String> result = new TreeSet<>((a, b) -> {
            if (a.length() != b.length()) {
                return Integer.compare(a.length(), b.length());
            }
            return a.compareTo(b);
        });
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try (InputStream is = cl.getResourceAsStream(filePath)) {
            if (is == null) {
                return Collections.emptySet();
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        result.add(line.replace(".", ""));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private void writeKeysToFile(Set<String> keysToRemove) {
        String filePath = "src/main/resources/store_remove_keys.txt";
        try (java.io.FileWriter fw = new java.io.FileWriter(filePath, StandardCharsets.UTF_8)) {
            for (String key : keysToRemove) {
                fw.write(key);
                fw.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
