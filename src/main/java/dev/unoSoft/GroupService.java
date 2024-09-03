package dev.unoSoft;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class GroupService {

    public List<Set<String>> fillGroups(List<Map<String, Set<String>>> columns) {
        List<Set<String>> groups = new ArrayList<>();
        for (Map<String, Set<String>> column : columns) {
            column.forEach((linePart, lines) -> {
                if (lines.size() > 1) {
                    Optional<Set<String>> existingGroup = groups.stream()
                            .filter(group -> group.stream().anyMatch(lines::contains))
                            .findFirst();

                    if (existingGroup.isPresent()) {
                        existingGroup.get().addAll(lines);
                    } else {
                        groups.add(lines);
                    }
                }
            });
        }
        return groups;
    }

    public void writeGroupsToFile(List<Set<String>> groups, String fileName) {
        groups.sort((group1, group2) -> Integer.compare(group2.size(), group1.size()));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            int groupNumber = 1;

            for (Set<String> group : groups) {
                if (group.size() > 1) {
                    bw.write("Группа " + groupNumber);
                    bw.newLine();

                    for (String line : group) {
                        bw.write(line);
                        bw.newLine();
                    }

                    bw.newLine();
                    groupNumber++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
