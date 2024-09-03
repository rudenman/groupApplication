package dev.unoSoft;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileProcessor {
    private final String filename;

    public FileProcessor(String filename) {
        this.filename = filename;
    }

    public boolean readColumnsFromFile(List<Map<String, Set<String>>> columns) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<Map<String, String>> singleLineParts = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.matches("^(\"[^\"]*\"(;|$))+$")) {
                    String[] lineParts = line.split(";");
                    for (int i = 0; i < lineParts.length; i++) {
                        if (columns.size() <= i) {
                            columns.add(new HashMap<>());
                            singleLineParts.add(new HashMap<>());
                        }
                        if (lineParts[i].equals("\"\"") || lineParts[i].isEmpty()) {
                            continue;
                        }
                        if (singleLineParts.get(i).containsKey(lineParts[i])) {
                            Set<String> lines = columns.get(i).computeIfAbsent(lineParts[i], k -> new HashSet<>());
                            lines.add(line);
                            lines.add(singleLineParts.get(i).get(lineParts[i]));
                        } else {
                            singleLineParts.get(i).put(lineParts[i], line);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        }
        return !columns.isEmpty();
    }
}
