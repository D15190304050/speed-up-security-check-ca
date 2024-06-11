package united.cn.suscc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import united.cn.suscc.domain.dtos.QuestionnaireInfo;
import united.cn.suscc.commons.ServiceResponse;
import united.cn.suscc.services.QuestionnaireService;

@Slf4j
@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController
{
    @Autowired
    private QuestionnaireService questionnaireService;

    public ServiceResponse<Boolean> submitQuestionnaire(QuestionnaireInfo questionnaireInfo)
    {
        return questionnaireService.submitQuestionnaire(questionnaireInfo);
    }

    public ServiceResponse<Long> getAverageWaitingDays()
    {
        return questionnaireService.getAverageWaitingDays();
    }


}
