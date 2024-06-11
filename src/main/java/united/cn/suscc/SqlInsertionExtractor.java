package united.cn.suscc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SqlInsertionExtractor
{
    private static final String[] ORDERED_LOCALES = {"en-us", "fr-fr", "zh-cn"};

    public static void main(String[] args)
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // 获取资源作为InputStream
        try (InputStream inputStream = classLoader.getResourceAsStream("options/options_of_application_type.csv"))
        {
            // 确保资源存在
            if (inputStream == null)
                throw new IllegalArgumentException("资源文件不存在");

            int code = 1;

            StringBuilder cmdInsertion = new StringBuilder("INSERT INTO `application_type`\n" +
                    "(`locale`, `code`, `option`)\n" +
                    "VALUES");

            // 使用BufferedReader读取内容
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
            while (scanner.hasNextLine())
            {
                String optionLine = scanner.nextLine();
                String[] options = optionLine.split(",");

                for (int i = 0; i < ORDERED_LOCALES.length; i++)
                    cmdInsertion.append(String.format("\n('%s', %d, '%s'),", ORDERED_LOCALES[i], code, options[i]));

                code++;
            }

            cmdInsertion.replace(cmdInsertion.length() - 1, cmdInsertion.length(), ";\n");
            System.out.println(cmdInsertion);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
