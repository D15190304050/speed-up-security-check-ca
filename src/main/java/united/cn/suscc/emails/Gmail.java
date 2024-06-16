package united.cn.suscc.emails;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * @author Tomorrow
 * @date 2020/11/2 17:41
 */
public class Gmail
{
    /*
     * gmail邮箱SSL方式
     */
    private static void gmailssl(Properties props)
    {
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        props.put("mail.debug", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
    }

    //gmail邮箱的TLS方式
    private static void gmailtls(Properties props)
    {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    /**
     * 创建邮件内容 需科学上网
     */
    public static boolean sendEmail(String sender, String password, String receiver, String content) throws Exception
    {
        //1.创建一封邮件的实例对象
        Properties props = new Properties();
        //选择ssl方式
        gmailtls(props);
//        gmailssl(props);

        // 当做多商户的时候需要使用getInstance, 如果只是一个邮箱发送的话就用getDefaultInstance
        // Session.getDefaultInstance 会将username,password保存在session会话中
        // Session.getInstance 不进行保存
        Session session = Session.getInstance(props,
                new Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(sender, password);
                    }
                });

        MimeMessage msg = new MimeMessage(session);
        //2.设置发件人地址
        msg.setFrom(new InternetAddress(sender));
        /**
         * 3.设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver));
        //4.设置邮件主题
        msg.setSubject("To reset your password!", "UTF-8");

        // 6. 创建文本"节点"
        MimeBodyPart text = new MimeBodyPart();
        // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
        text.setContent(content,
                "text/html;charset=UTF-8");

        // 7. （文本+图片）设置 文本 和 图片"节点"的关系（将 文本 和 图片"节点"合成一个混合"节点"）
        MimeMultipart mm_text_image = new MimeMultipart();
        mm_text_image.addBodyPart(text);
        mm_text_image.setSubType("related");    // 关联关系

        // 11. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
        msg.setContent(mm_text_image);
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());

        Transport.send(msg);
        return true;
    }

    public static void main(String[] args) throws Exception
    {

        final String sender = "iamnotyourspy@gmail.com";// gmail 邮箱
        final String password = "kvhq jqeu ilms qwhb";// Google应用专用密码

        String receiverEmail = "15190304050@163.com";

        String content = "<p>亲爱的用户:</p>" +
                "<p>&nbsp; &nbsp; 该邮件为测试邮件（修改密码）:</p>" +
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;\u200B" + 1234 + "&nbsp;(为了保障您帐号的安全性，请尽快修改密码)</p>" +
                "<p><br></p><p>该邮件为系统自动发送, 请勿进行回复!</p>" +
                "<p><br></p>";

        boolean mimeMessage = sendEmail(sender, password, receiverEmail, content);
        if (mimeMessage)
        {
            System.out.println("The email sends successfully!");
        }
        else
        {
            System.out.println("The email sends failed!");
        }
        System.out.println();
    }
}

