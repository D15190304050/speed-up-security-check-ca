package united.cn.suscc.preparation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.DirectoryIteratorException;
import java.util.Scanner;

public class SqlInsertionExtractor
{
    public static final String FILE_NAME_PREFIX = "options_of_";
    public static final String FILE_NAME_SUFFIX = ".csv";
    public static final String RESULT_SQL_FILE_NAME = "result.sql";
    public static final String OPTIONS_DIRECTORY_NAME = "options/";
    public static final String CONFIGURATION_FILE_NAME = "application.yaml";
    public static final String OTHER = "Other";

    private static final String[] ORDERED_LOCALES = {"en-us", "fr-fr", "zh-cn"};

    public static void main(String[] args) throws IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        URL resource = classLoader.getResource(CONFIGURATION_FILE_NAME);
        if (resource == null)
            throw new FileNotFoundException("File " + CONFIGURATION_FILE_NAME + " not found.");

        String configurationFilePath = resource.getPath();
        int lastIndexOfSlash = configurationFilePath.lastIndexOf("/");
        String resourcesDirectoryPath = configurationFilePath.substring(0, lastIndexOfSlash + 1);
        String optionsDirectoryPath = resourcesDirectoryPath + OPTIONS_DIRECTORY_NAME;

        File optionsDirectory = new File(optionsDirectoryPath);
        File[] optionFiles = optionsDirectory.listFiles();

        if (optionFiles == null)
            throw new DirectoryIteratorException(new IOException("Can not get files in " + optionsDirectoryPath + "."));

        StringBuilder allCmdInsertions = new StringBuilder();

        for (File optionFile : optionFiles)
        {
            String fileName = optionFile.getName();

            if (fileName.startsWith(FILE_NAME_PREFIX) && fileName.endsWith(FILE_NAME_SUFFIX))
            {
                StringBuilder cmdInsertion = getCmdInsertionForTable(optionFile, fileName);
                allCmdInsertions.append(cmdInsertion);
            }
        }

        PrintWriter writer = new PrintWriter(optionsDirectoryPath + RESULT_SQL_FILE_NAME);
        writer.println(allCmdInsertions);
        writer.flush();
        writer.close();
    }

    private static StringBuilder getCmdInsertionForTable(File optionFile, String fileName) throws FileNotFoundException
    {
        int lastIndexOfFileNameSuffix = fileName.lastIndexOf(FILE_NAME_SUFFIX);
        String tableName = fileName.substring(FILE_NAME_PREFIX.length(), lastIndexOfFileNameSuffix);

        StringBuilder cmdInsertion = new StringBuilder("INSERT INTO `" + tableName + "`\n" +
                "(`locale`, `code`, `option`)\n" +
                "VALUES");

        int code = 1;

        Scanner input = new Scanner(optionFile);

        while (input.hasNext())
        {
            String optionLine = input.nextLine();
            String[] options = optionLine.split(",");

            int nextCode = OTHER.equalsIgnoreCase(options[0]) ? -1 : code;

            for (int i = 0; i < ORDERED_LOCALES.length; i++)
                cmdInsertion.append(String.format("\n('%s', %d, '%s'),", ORDERED_LOCALES[i], nextCode, options[i].replace("'", "\\'")));

            code++;
        }

        input.close();

        cmdInsertion.replace(cmdInsertion.length() - 1, cmdInsertion.length(), ";\n\n");
        return cmdInsertion;
    }
}
