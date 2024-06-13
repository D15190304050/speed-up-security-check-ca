package united.cn.suscc.domain.entities;

import lombok.Data;

@Data
public class QuestionnaireOptionBase
{
    private long id;
    private String locale;
    private int code;
    private String optionText;
}
