package united.cn.suscc.domain.dtos;

import lombok.Data;

import java.util.List;

@Data
public class LocaleQuestionnaireOption
{
    private String locale;
    private List<QuestionnaireOption> applicationStates;
    private List<QuestionnaireOption> applicationSubmissionLocations;
    private List<QuestionnaireOption> applicationTypes;
    private List<QuestionnaireOption> countriesOfResidence;
    private List<QuestionnaireOption> currentPassportCountries;
    private List<QuestionnaireOption> genders;
}
