package dev.unoSoft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupApplication {

    private final String OUTPUT_FILE = "output.txt";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar groupApplication.jar <filename>");
            return;
        }

        GroupApplication app = new GroupApplication();
        app.run(args[0]);

    }

    private void run(String filename) {
        List<Map<String, Set<String>>> columns = new ArrayList<>();

        FileProcessor fileProcessor = new FileProcessor(filename);
        boolean generatingColumnsSuccess = fileProcessor.readColumnsFromFile(columns);
        if (!generatingColumnsSuccess) {
            return;
        }

        GroupService groupService = new GroupService();
        List<Set<String>> groups = groupService.fillGroups(columns);

        groupService.writeGroupsToFile(groups, OUTPUT_FILE);
    }


}
