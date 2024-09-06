package dev.unoSoft;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupService {

    public void outputGroupsToFile(Set<Set<String>> groups, String fileName) {
        List<Set<String>> groupList = new ArrayList<>(groups);
        groupList.sort((group1, group2) -> Integer.compare(group2.size(), group1.size()));


        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            int groupNumber = 1;

            bw.write("The number of multi-element groups: " + groupList.size());
            bw.newLine();
            bw.newLine();

            for (Set<String> group : groupList) {
                if (group.size() > 1) {
                    bw.write("Group: " + groupNumber);
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

    public Set<Set<String>> mergeGroups(Set<Set<String>> groups) {
        Map<String, String> parent = new HashMap<>();
        Map<String, Set<String>> components = new HashMap<>();

        for (Set<String> group : groups) {
            for (String str : group) {
                parent.putIfAbsent(str, str);
            }
        }

        for (Set<String> group : groups) {
            Iterator<String> iterator = group.iterator();
            if (iterator.hasNext()) {
                String first = find(iterator.next(), parent);
                while (iterator.hasNext()) {
                    String current = find(iterator.next(), parent);
                    union(first, current, parent);
                }
            }
        }

        for (String str : parent.keySet()) {
            String root = find(str, parent);
            components.computeIfAbsent(root, k -> new HashSet<>()).add(str);
        }

        return new HashSet<>(components.values());
    }

    private String find(String str, Map<String, String> parent) {
        if (!str.equals(parent.get(str))) {
            parent.put(str, find(parent.get(str), parent));
        }
        return parent.get(str);
    }

    private void union(String str1, String str2, Map<String, String> parent) {
        String root1 = find(str1, parent);
        String root2 = find(str2, parent);
        if (!root1.equals(root2)) {
            parent.put(root2, root1);
        }
    }
}
