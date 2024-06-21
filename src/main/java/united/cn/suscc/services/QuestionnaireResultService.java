package united.cn.suscc.services;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import united.cn.suscc.commons.JsonSerializer;
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

@Slf4j
@Service
@Validated
public class QuestionnaireResultService
{
    public static final int CODE_OF_PASS = 2;

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

        // Validation 9.
        long countByEmail = questionnaireResultMapper.countByEmail(questionnaireInfo.getEmailAddress());
        if (countByEmail > 0)
            return "You have already submitted a questionnaire by this email.";

        // Validation 1.
        Date applicationSubmissionDate = questionnaireInfo.getApplicationSubmissionDate();
        Date applicationEndDate = questionnaireInfo.getApplicationEndDate();

        if (applicationEndDate != null)
        {
            if (applicationEndDate.before(applicationSubmissionDate))
                return "Your application submission date is before the application end date.";
        }

        // Validation 2.
        Date dateOfEnteringSecurityScreening = questionnaireInfo.getDateOfEnteringSecurityScreening();
        if (dateOfEnteringSecurityScreening.before(applicationSubmissionDate))
            return "Your application submission date is before the date of entering security screening.";

        // Validation 3 & 4.
        Date dateOfClearingSecurityScreening = questionnaireInfo.getDateOfClearingSecurityScreening();
        int currentApplicationState = questionnaireInfo.getCurrentApplicationState();
        if (dateOfClearingSecurityScreening != null)
        {
            // Validation 3.
            if (dateOfClearingSecurityScreening.before(dateOfEnteringSecurityScreening))
                return "Your date of clearing security screening is before the date of entering security screening.";

            // Validation 4.
            if (currentApplicationState != CODE_OF_PASS)
                return "Your current application state is incorrect. You provided the date of clearing security screening, but the current application state is not passed.";
        }

        // Validation 5.
        if (currentApplicationState == CODE_OF_PASS)
        {
            if (dateOfClearingSecurityScreening == null)
                return "Please provide your date of clearing security screening since your state is pass now.";
        }

        // Validation 6.
        if (applicationEndDate != null && dateOfEnteringSecurityScreening.after(applicationEndDate))
            return "Your date of entering security screening is after the application end date.";

        // Validation 7.
        if (dateOfClearingSecurityScreening != null && dateOfClearingSecurityScreening.after(applicationEndDate))
            return "Your date of clearing security screening is after the application end date.";

        // Validation 8.
        Date lastUpdateDateFromIrcc = questionnaireInfo.getLastUpdateDateFromIrcc();
        if (lastUpdateDateFromIrcc.before(applicationSubmissionDate))
            return "Your last update date is before the application submission date.";

        return null;
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
