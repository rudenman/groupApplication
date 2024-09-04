package dev.unoSoft;

import java.util.Set;

public class GroupApplication {

    private final String OUTPUT_FILE_PATH = "output.txt";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar groupApplication.jar <filename>");
            return;
        }

        GroupApplication app = new GroupApplication();
        app.run(args[0]);
    }

    private void run(String filename) {
        FileDataToDirtyGroupProcessor fileDataToDirtyGroupProcessor = new FileDataToDirtyGroupProcessor(filename);
        Set<Set<String>> groups = fileDataToDirtyGroupProcessor.readDirtyGroupsFromFile();
        if (groups.isEmpty()) {
            return;
        }

        GroupService groupService = new GroupService();
        groups = groupService.mergeGroups(groups);

        groupService.outputGroupsToFile(groups, OUTPUT_FILE_PATH);
    }
}
