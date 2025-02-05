package united.cn.suscc.services;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import united.cn.suscc.commons.JsonSerializer;
import united.cn.suscc.commons.LocaleConverter;
import united.cn.suscc.dao.QuestionnaireResultMapper;
import united.cn.suscc.domain.dtos.AverageWaitingDaysByApplicationType;
import united.cn.suscc.domain.dtos.QuestionnaireInfo;
import united.cn.suscc.commons.ServiceResponse;
import united.cn.suscc.domain.dtos.WaitingDaysResponse;
import united.cn.suscc.domain.entities.QuestionnaireResult;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@Validated
public class QuestionnaireResultService
{
    public static final int CODE_OF_IN_PROGRESS = 1;
    public static final int CODE_OF_PASS = 2;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private GmailService gmailService;

    @Autowired
    private QuestionnaireResultMapper questionnaireResultMapper;

    public ServiceResponse<Boolean> submitQuestionnaire(@Valid QuestionnaireInfo questionnaireInfo) throws TemplateException, MessagingException, IOException
    {
        log.info("questionnaireInfo = {}", JsonSerializer.serialize(questionnaireInfo));
        String errorMessage = validateQuestionnaireInfo(questionnaireInfo);
        if (StringUtils.hasText(errorMessage))
            return ServiceResponse.buildErrorResponse(-100, errorMessage);

        // Save to database.
        QuestionnaireResult questionnaireResult = new QuestionnaireResult();
        BeanUtils.copyProperties(questionnaireInfo, questionnaireResult);
        questionnaireResultMapper.insert(questionnaireResult);
        log.info("Save questionnaire result to database successfully.");

        // Send verification email.
        gmailService.sendVerificationEmail(questionnaireInfo.getEmailAddress(), questionnaireInfo.getCurrentLanguage());
        log.info("Send verification email successfully.");

        return ServiceResponse.buildSuccessResponse(true);
    }

    private String validateQuestionnaireInfo(QuestionnaireInfo questionnaireInfo)
    {
        // Validations:
        // 1. applicationSubmissionDate must be before applicationEndDate.
        // 2. dateOfEnteringSecurityScreening must be after applicationSubmissionDate.
        // 3. dateOfClearingSecurityScreening must be after dateOfEnteringSecurityScreening.
        // 4. dateOfClearingSecurityScreening is provided but currentApplicationState is not passed.
        // 5. Ask the user to provide dateOfClearingSecurityScreening if currentApplicationState is "pass".
        // 6. dateOfEnteringSecurityScreening must be before applicationEndDate.
        // 7. dateOfClearingSecurityScreening must be before applicationEndDate.
        // 8. lastUpdateDateFromIrcc must be after applicationSubmissionDate.
        // 9. Existence of the email address.

        Locale locale = getQuestionnaireInfoLocale(questionnaireInfo);

        // Validation 9.
        long countByEmail = questionnaireResultMapper.countByEmail(questionnaireInfo.getEmailAddress());
        if (countByEmail > 0)
            return messageSource.getMessage("questionnaire.verification.email-submitted", null, locale);

        // Validation 1.
        Date applicationSubmissionDate = questionnaireInfo.getApplicationSubmissionDate();
        Date applicationEndDate = questionnaireInfo.getApplicationEndDate();

        if (applicationEndDate != null)
        {
            if (applicationEndDate.before(applicationSubmissionDate))
                return messageSource.getMessage("questionnaire.verification.submission-date-before-application-end", null, locale);
        }

        // Validation 2.
        Date dateOfEnteringSecurityScreening = questionnaireInfo.getDateOfEnteringSecurityScreening();
        if (dateOfEnteringSecurityScreening.before(applicationSubmissionDate))
            return messageSource.getMessage("questionnaire.verification.submission-date-before-entering-security-screening", null, locale);

        // Validation 3 & 4.
        Date dateOfClearingSecurityScreening = questionnaireInfo.getDateOfClearingSecurityScreening();
        int currentApplicationState = questionnaireInfo.getCurrentApplicationState();
        if (dateOfClearingSecurityScreening != null)
        {
            // Validation 3.
            if (dateOfClearingSecurityScreening.before(dateOfEnteringSecurityScreening))
                return messageSource.getMessage("questionnaire.verification.enter-security-screening-before-entering-security-screening", null, locale);

            // Validation 4.
            if (currentApplicationState != CODE_OF_IN_PROGRESS)
                return messageSource.getMessage("questionnaire.verification.clearing-security-screening-but-in-progress", null, locale);
        }

        // Validation 5.
        if (currentApplicationState != CODE_OF_IN_PROGRESS)
        {
            if (dateOfClearingSecurityScreening == null)
                return messageSource.getMessage("questionnaire.verification.security-screening-ended-without-clearing-date", null, locale);
        }

        // Validation 6.
        if (applicationEndDate != null && dateOfEnteringSecurityScreening.after(applicationEndDate))
            return messageSource.getMessage("questionnaire.verification.enter-security-screening-after-application-end", null, locale);

        // Validation 7.
        if (dateOfClearingSecurityScreening != null && dateOfClearingSecurityScreening.after(applicationEndDate))
            return messageSource.getMessage("questionnaire.verification.clear-security-screening-after-application-end", null, locale);

        // Validation 8.
        Date lastUpdateDateFromIrcc = questionnaireInfo.getLastUpdateDateFromIrcc();
        if (lastUpdateDateFromIrcc.before(applicationSubmissionDate))
            return messageSource.getMessage("questionnaire.verification.update-before-application", null, locale);

        Date now = new Date();
        if (applicationSubmissionDate.after(now) ||
                (applicationEndDate != null && applicationEndDate.after(now)) ||
                dateOfEnteringSecurityScreening.after(now) ||
                (dateOfClearingSecurityScreening != null && dateOfClearingSecurityScreening.after(now)) ||
                lastUpdateDateFromIrcc.after(now))
            return messageSource.getMessage("questionnaire.verification.all-date-before-now", null, locale);

        return null;
    }

    private static Locale getQuestionnaireInfoLocale(QuestionnaireInfo questionnaireInfo)
    {
        String currentLanguage = questionnaireInfo.getCurrentLanguage();
        return LocaleConverter.toLanguageCountry(currentLanguage);
    }

    public ServiceResponse<WaitingDaysResponse> getStatisticsData()
    {
        List<AverageWaitingDaysByApplicationType> averageWaitingDayOfTypes = questionnaireResultMapper.getAverageWaitingDaysByApplicationType();
        BigDecimal averageWaitingDays = questionnaireResultMapper.getAverageWaitingDays();

        WaitingDaysResponse waitingDaysResponse = new WaitingDaysResponse();
        waitingDaysResponse.setAverageWaitingDaysOfApplicationTypes(averageWaitingDayOfTypes);
        waitingDaysResponse.setAverageWaitingDays(averageWaitingDays);

        return ServiceResponse.buildSuccessResponse(waitingDaysResponse);
    }
}
