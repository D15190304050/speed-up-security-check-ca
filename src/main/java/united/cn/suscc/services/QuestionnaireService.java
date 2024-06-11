package united.cn.suscc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import united.cn.suscc.dao.QuestionnaireMapper;
import united.cn.suscc.domain.dtos.QuestionnaireInfo;
import united.cn.suscc.commons.ServiceResponse;
import united.cn.suscc.domain.entities.QuestionnaireResult;

@Slf4j
@Service
public class QuestionnaireService
{
    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    public ServiceResponse<Boolean> submitQuestionnaire(QuestionnaireInfo questionnaireInfo)
    {
        QuestionnaireResult questionnaireResult = new QuestionnaireResult();
        BeanUtils.copyProperties(questionnaireInfo, questionnaireResult);
        questionnaireMapper.insert(questionnaireResult);

        return ServiceResponse.buildSuccessResponse(true);
    }

    public ServiceResponse<Long> getAverageWaitingDays()
    {
        long averageWaitingDays = questionnaireMapper.getAverageWaitingDays();
        return ServiceResponse.buildSuccessResponse(averageWaitingDays);
    }
}
