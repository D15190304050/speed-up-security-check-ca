package united.cn.suscc.services;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import united.cn.suscc.dao.EmailVerificationRecordMapper;
import united.cn.suscc.domain.entities.EmailVerificationRecord;
import united.cn.suscc.emails.EmailRenderer;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class GmailService
{
    private static final Properties PROPERTIES_FOR_TTL;

    @Value("${mail.sender}")
    private String sender;

    @Value("${mail.password}")
    private String password;

    @Value("${front-end-address.base}")
    private String frontendAddress;

    @Value("${front-end-address.verification}")
    private String frontendPathForVerification;

    @Autowired
    private EmailVerificationRecordMapper emailVerificationRecordMapper;

    static
    {
        PROPERTIES_FOR_TTL = new Properties();
        PROPERTIES_FOR_TTL.put("mail.smtp.auth", "true");
        PROPERTIES_FOR_TTL.put("mail.smtp.starttls.enable", "true");
        PROPERTIES_FOR_TTL.put("mail.smtp.host", "smtp.gmail.com");
        PROPERTIES_FOR_TTL.put("mail.smtp.port", "587");
    }

    public void sendEmail(List<String> receivers, String subject, String content) throws MessagingException
    {
        Session session = Session.getInstance(PROPERTIES_FOR_TTL,
                new Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(sender, password);
                    }
                });

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sender));
        for (String receiver : receivers)
            msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver));

        msg.setSubject(subject, "UTF-8");

        // 6. 创建文本"节点"
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(content, "text/html;charset=UTF-8");

        MimeMultipart mm_text_image = new MimeMultipart();
        mm_text_image.addBodyPart(text);
        mm_text_image.setSubType("related");

        msg.setContent(mm_text_image);
        msg.setSentDate(new Date());

        Transport.send(msg);
    }

    public void sendVerificationEmail(String receiverEmailAddress) throws TemplateException, IOException, MessagingException
    {
        String verificationLinkCode = getVerificationLinkCode(receiverEmailAddress);
        String verificationLink = getVerificationLink(verificationLinkCode);

        saveEmailVerificationRecord(receiverEmailAddress, verificationLinkCode);

        HashMap<String, Object> dataModel = new HashMap<>();
        dataModel.put("link", verificationLink);
        String content = EmailRenderer.renderEmailTemplate("verification_zh.ftl", dataModel);

        List<String> receivers = List.of(receiverEmailAddress);
        sendEmail(receivers, "加拿大签证安调问卷 - 请勿回复", content);
    }

    private String getVerificationLinkCode(String receiverEmailAddress)
    {
        String uuid = UUID.randomUUID().toString();
        String emailAddressWithUuid = receiverEmailAddress + "?" + uuid;
        return Base64.getEncoder().encodeToString(emailAddressWithUuid.getBytes(StandardCharsets.UTF_8));
    }

    private String getVerificationLink(String verificationLinkCode)
    {
        return frontendAddress + frontendPathForVerification + "?" + EmailLinkVerificationService.VERIFICATION_PARAM + "=" + verificationLinkCode;
    }

    private void saveEmailVerificationRecord(String receiverEmailAddress, String verificationLinkCode)
    {
        Instant instant = Instant.now();
        Date now = Date.from(instant);
        Date expirationDate = Date.from(Instant.now().plus(10, ChronoUnit.DAYS));

        EmailVerificationRecord emailVerificationRecord = new EmailVerificationRecord();
        emailVerificationRecord.setEmailAddress(receiverEmailAddress);
        emailVerificationRecord.setCode(verificationLinkCode);
        emailVerificationRecord.setCreationTime(now);
        emailVerificationRecord.setExpirationTime(expirationDate);
        emailVerificationRecord.setState(0);

        emailVerificationRecordMapper.insert(emailVerificationRecord);
    }
}
