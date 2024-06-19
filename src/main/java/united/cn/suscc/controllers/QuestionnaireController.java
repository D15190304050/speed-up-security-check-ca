package united.cn.suscc.controllers;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import united.cn.suscc.domain.dtos.LocaleQuestionnaireOption;
import united.cn.suscc.domain.dtos.QuestionnaireInfo;
import united.cn.suscc.commons.ServiceResponse;
import united.cn.suscc.services.QuestionnaireOptionsService;
import united.cn.suscc.services.QuestionnaireService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController
{
    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private QuestionnaireOptionsService questionnaireOptionsService;

    @PostMapping("/submit")
    public ServiceResponse<Boolean> submitQuestionnaire(@RequestBody QuestionnaireInfo questionnaireInfo) throws TemplateException, MessagingException, IOException
    {
        return questionnaireService.submitQuestionnaire(questionnaireInfo);
    }

    @GetMapping("/statistics")
    public ServiceResponse<Long> getStatisticsData()
    {
        return questionnaireService.getStatisticsData();
    }

    @GetMapping("/options")
    public ServiceResponse<List<LocaleQuestionnaireOption>> getAllOptions()
    {
        return questionnaireOptionsService.getAllOptionsFromCache();
    }

    @GetMapping("/refresh")
    public ServiceResponse<Boolean> refreshOptions(@RequestParam("secret") String secret)
    {
        return questionnaireOptionsService.refreshOptions(secret);
    }
}
