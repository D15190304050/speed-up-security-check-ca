package united.cn.suscc.services;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import united.cn.suscc.domain.dtos.LocaleQuestionnaireOption;
import united.cn.suscc.domain.dtos.QuestionnaireOptionsHolder;

import java.util.List;

@Component
public class QuestionnaireOptionsInitializer implements ApplicationContextAware, ApplicationListener<WebServerInitializedEvent>
{
    @Autowired
    private QuestionnaireOptionService1 questionnaireOptionService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {

    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event)
    {
        List<LocaleQuestionnaireOption> allOptions = questionnaireOptionService.getAllOptions();
        QuestionnaireOptionsHolder.setLocaleQuestionnaireOptions(allOptions);
    }
}
