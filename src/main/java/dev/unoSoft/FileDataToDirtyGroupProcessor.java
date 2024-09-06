package dev.unoSoft;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FileDataToDirtyGroupProcessor {
    private final String REG_EXP = "^;*((\"[^\"]*\")|[^;]*)(;+(\"[^\"]*\")|[^;]*)*;*$";
    private final String filename;
    private Set<String> SingleGroupSet;
    private Set<Set<String>> multipleGroupSet;

    public FileDataToDirtyGroupProcessor(String filename) {
        this.filename = filename;
        SingleGroupSet = new HashSet<>();
        multipleGroupSet = new HashSet<>();
    }

    public boolean readDirtyGroupsFromFile() {
        Map<LinePart, String> linePartToDirtySingleGroupMap = new HashMap<>();
        Map<LinePart, Set<String>> linePartToDirtyMultipleGroupMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.matches(REG_EXP)) {
                    continue;
                }
                String[] lineParts = line.split(";");
                for (int i = 0; i < lineParts.length; i++) {
                    if (lineParts[i].equals("\"\"") || lineParts[i].isEmpty()) {
                        continue;
                    }
                    LinePart linePart = new LinePart(lineParts[i], i);
                    if (linePartToDirtySingleGroupMap.containsKey(linePart)) {
                        Set<String> group = linePartToDirtyMultipleGroupMap.computeIfAbsent(linePart, v -> new HashSet<>());
                        group.add(line);
                        group.add(linePartToDirtySingleGroupMap.get(linePart));
                    } else {
                        linePartToDirtySingleGroupMap.put(linePart, line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return false;
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            return false;
        }
        SingleGroupSet = linePartToDirtySingleGroupMap.entrySet().stream()
                .filter(entry -> !linePartToDirtyMultipleGroupMap.containsKey(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());

        multipleGroupSet = linePartToDirtyMultipleGroupMap.values().stream()
                .filter(group -> group.size() > 1)
                .collect(Collectors.toSet());
        return true;
    }

    public Set<String> getSingleGroupSet() {
        return SingleGroupSet;
    }

    public Set<Set<String>> getMultipleGroupSet() {
        return multipleGroupSet;
    }
}
