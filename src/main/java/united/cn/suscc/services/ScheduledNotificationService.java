package united.cn.suscc.services;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import united.cn.suscc.dao.QuestionnaireResultMapper;
import united.cn.suscc.domain.dtos.EmailAddressAndWaitingDaysInfo;
import united.cn.suscc.emails.EmailRenderer;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ScheduledNotificationService
{
    private static final String EMAIL_SUBJECT = "Notification";
    private static final String TEMPLATE_NAME = "scheduled-notification_en.ftl";
    private static final SimpleDateFormat dateFormat;

    static
    {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Autowired
    private EmailService emailService;

    @Autowired
    private QuestionnaireResultMapper questionnaireResultMapper;

    @Autowired
    private ThreadPoolTaskExecutor emailTaskExecutor;

    @Scheduled(cron = "0 0 10 * * MON", zone = "Asia/Shanghai")
    public void sendScheduledEmail() throws MessagingException, TemplateException, IOException
    {
        List<EmailAddressAndWaitingDaysInfo> emailAddressAndWaitingDaysInfoList = questionnaireResultMapper.getEmailAddressAndWaitingDays();

        for (EmailAddressAndWaitingDaysInfo user : emailAddressAndWaitingDaysInfoList)
        {
            log.info("Starting email sending task for user: {}", user.getEmailAddress());
            CompletableFuture.runAsync(() ->
            {
                try
                {
                    String emailAddress = user.getEmailAddress();
                    Date applicationSubmissionDate = user.getApplicationSubmissionDate();
                    String formattedDate = dateFormat.format(applicationSubmissionDate);

                    HashMap<String, Object> dataModel = new HashMap<>();
                    dataModel.put("waitingDays", user.getWaitingDays());
                    dataModel.put("applicationSubmissionDate", formattedDate);

                    String emailContent = EmailRenderer.renderEmailTemplate(TEMPLATE_NAME, dataModel);

                    emailService.sendEmail(List.of(emailAddress), EMAIL_SUBJECT, emailContent);
                }
                catch (Exception e)
                {
                    log.error("Error in sending scheduled emails with ", e);
                }
            }, emailTaskExecutor);
        }
    }
}
