package united.cn.suscc.domain.dtos;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import united.cn.suscc.domain.entities.QuestionnaireOptionBase;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionnaireOption
{
    private int code;
    private String optionText;

    public static <T extends QuestionnaireOptionBase> QuestionnaireOption toQuestionnaireOption(T optionBase)
    {
        QuestionnaireOption option = new QuestionnaireOption();
        option.setCode(optionBase.getCode());
        option.setOptionText(optionBase.getOptionText());
        return option;
    }

    public static <T extends QuestionnaireOptionBase> List<QuestionnaireOption> toQuestionnaireOptions(List<T> optionBases)
    {
        if (CollectionUtils.isEmpty(optionBases))
            return new ArrayList<>();

        List<QuestionnaireOption> options = new ArrayList<>();
        for (QuestionnaireOptionBase optionBase : optionBases)
            options.add(toQuestionnaireOption(optionBase));
        return options;
    }
}
