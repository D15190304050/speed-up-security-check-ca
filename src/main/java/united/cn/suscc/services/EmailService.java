package united.cn.suscc.services;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import united.cn.suscc.commons.LocaleConverter;
import united.cn.suscc.dao.EmailVerificationRecordMapper;
import united.cn.suscc.domain.entities.EmailVerificationRecord;
import united.cn.suscc.emails.EmailRenderer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class EmailService
{

    public static final String VERIFICATION_EMAIL_TEMPLATE_NAME_PREFIX = "verification_";
    public static final String VERIFICATION_EMAIL_TEMPLATE_NAME_SUFFIX = ".ftl";

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${front-end-address.base}")
    private String frontendAddress;

    @Value("${front-end-address.verification}")
    private String frontendPathForVerification;

    @Autowired
    private MessageSource messageSource;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    private EmailVerificationRecordMapper emailVerificationRecordMapper;

    public void sendEmail(List<String> receivers, String subject, String content) throws MessagingException
    {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        helper = new MimeMessageHelper(message, true);
        helper.setFrom(sender);
        helper.setTo(receivers.toArray(new String[0]));
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
        log.info("Email sent successfully to: {}", receivers);
    }

    public void sendVerificationEmail(String receiverEmailAddress, String currentLanguage) throws MessagingException, TemplateException, IOException
    {
        String verificationLinkCode = getVerificationLinkCode(receiverEmailAddress);
        String verificationLink = getVerificationLink(verificationLinkCode);

        saveEmailVerificationRecord(receiverEmailAddress, verificationLinkCode);

        HashMap<String, Object> dataModel = new HashMap<>();
        dataModel.put("link", verificationLink);

        String templateName = VERIFICATION_EMAIL_TEMPLATE_NAME_PREFIX + currentLanguage + VERIFICATION_EMAIL_TEMPLATE_NAME_SUFFIX;
        String emailContent = EmailRenderer.renderEmailTemplate(templateName, dataModel);

        List<String> receivers = List.of(receiverEmailAddress);
        String subject = messageSource.getMessage("email.verification.subject", null, LocaleConverter.toLanguageCountry(currentLanguage));
        sendEmail(receivers, subject, emailContent);
    }

    private String getVerificationLinkCode(String receiverEmailAddress)
    {
        String uuid = UUID.randomUUID().toString();
        String emailAddressWithUuid = receiverEmailAddress + EmailLinkVerificationService.EMAIL_CODE_SEPARATOR + uuid;
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
        emailVerificationRecord.setUpdateTime(now);
        emailVerificationRecord.setExpirationTime(expirationDate);
        emailVerificationRecord.setState(0);

        emailVerificationRecordMapper.insert(emailVerificationRecord);
    }
}