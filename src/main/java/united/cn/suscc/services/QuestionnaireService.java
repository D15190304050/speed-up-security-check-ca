package united.cn.suscc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import united.cn.suscc.commons.JsonSerializer;
import united.cn.suscc.dao.QuestionnaireMapper;
import united.cn.suscc.domain.dtos.QuestionnaireInfo;
import united.cn.suscc.commons.ServiceResponse;
import united.cn.suscc.domain.entities.QuestionnaireResult;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@Service
@Validated
public class QuestionnaireService
{
    public static final int CODE_OF_PASS = 2;

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    public ServiceResponse<Boolean> submitQuestionnaire(@Valid QuestionnaireInfo questionnaireInfo)
    {
        log.info("questionnaireInfo = {}", JsonSerializer.serialize(questionnaireInfo));
        String errorMessage = validateQuestionnaireInfo(questionnaireInfo);
        if (StringUtils.hasText(errorMessage))
            return ServiceResponse.buildErrorResponse(-100, errorMessage);

        QuestionnaireResult questionnaireResult = new QuestionnaireResult();
        BeanUtils.copyProperties(questionnaireInfo, questionnaireResult);
        questionnaireMapper.insert(questionnaireResult);

        return ServiceResponse.buildSuccessResponse(true);
    }

    private static String validateQuestionnaireInfo(QuestionnaireInfo questionnaireInfo)
    {
        // Validations:
        // 1. applicationSubmissionDate must be before applicationEndDate.
        // 2. dateOfEnteringSecurityScreening must be after applicationSubmissionDate.
        // 3. dateOfClearingSecurityScreening must be after dateOfEnteringSecurityScreening.
        // 4. Ask the user to provide dateOfClearingSecurityScreening if currentApplicationState is "pass".

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

        // Validation 3.
        Date dateOfClearingSecurityScreening = questionnaireInfo.getDateOfClearingSecurityScreening();
        if (dateOfClearingSecurityScreening != null)
        {
            if (dateOfClearingSecurityScreening.before(dateOfEnteringSecurityScreening))
                return "Your date of clearing security screening is before the date of entering security screening.";
        }

        // Validation 4.
        int currentApplicationState = questionnaireInfo.getCurrentApplicationState();
        if (currentApplicationState == CODE_OF_PASS)
        {
            if (dateOfClearingSecurityScreening == null)
                return "Please provide your date of clearing security screening since your state is pass now.";
        }

//        questionnaireInfo.getSeparatedWithFamily();

        return null;
    }

    public ServiceResponse<Long> getStatisticsData()
    {
        long averageWaitingDays = questionnaireMapper.getAverageWaitingDays();
        return ServiceResponse.buildSuccessResponse(averageWaitingDays);
    }
}
