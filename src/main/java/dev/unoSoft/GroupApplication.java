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

        if (!fileDataToDirtyGroupProcessor.readDirtyGroupsFromFile()) {
            return;
        }

        Set<Set<String>> multipleGroups = fileDataToDirtyGroupProcessor.getMultipleGroupSet();
        Set<String> singleGroups = fileDataToDirtyGroupProcessor.getSingleGroupSet();

        GroupService groupService = new GroupService();
        multipleGroups = groupService.mergeGroups(multipleGroups);

        groupService.outputGroupsToFile(multipleGroups, singleGroups, OUTPUT_FILE_PATH);
    }
}
