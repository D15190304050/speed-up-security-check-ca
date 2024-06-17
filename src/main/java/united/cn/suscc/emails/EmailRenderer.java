package united.cn.suscc.emails;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class EmailRenderer
{
    public static final String EMAIL_TEMPLATES_DIRECTORY_PATH = "/emailTemplates/";
    private static final Configuration FREE_MARKER_CONFIGURATION;

    static
    {
        FREE_MARKER_CONFIGURATION = new Configuration(Configuration.VERSION_2_3_33);
        FREE_MARKER_CONFIGURATION.setClassForTemplateLoading(EmailRenderer.class, EMAIL_TEMPLATES_DIRECTORY_PATH);
    }

    public static String renderEmailTemplate(String templateName, Map<String, Object> dataModel) throws IOException, TemplateException
    {
        // 获取模板
        Template template = FREE_MARKER_CONFIGURATION.getTemplate(templateName);

        // 使用数据模型和模板渲染输出
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);

        return writer.toString();
    }

    public static void main(String[] args)
    {
        try
        {
            // 准备数据模型
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("user", new User("张三", 33));

            // 渲染模板
            String emailContent = renderEmailTemplate("test.ftl", dataModel);
            System.out.println(emailContent); // 打印或使用此内容发送邮件
        }
        catch (IOException | TemplateException e)
        {
            e.printStackTrace();
        }
    }
}