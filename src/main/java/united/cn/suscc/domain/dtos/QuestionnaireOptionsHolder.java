package united.cn.suscc.domain.dtos;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireOptionsHolder
{
    private QuestionnaireOptionsHolder()
    {}

    private static final List<LocaleQuestionnaireOption> LOCALE_QUESTIONNAIRE_OPTIONS;

    static
    {
        LOCALE_QUESTIONNAIRE_OPTIONS = new ArrayList<>();
    }

    public static List<LocaleQuestionnaireOption> getLocaleQuestionnaireOptions()
    {
        return LOCALE_QUESTIONNAIRE_OPTIONS;
    }

    public static void setLocaleQuestionnaireOptions(List<LocaleQuestionnaireOption> localeQuestionnaireOptions)
    {
        LOCALE_QUESTIONNAIRE_OPTIONS.clear();
        LOCALE_QUESTIONNAIRE_OPTIONS.addAll(localeQuestionnaireOptions);
    }
}
