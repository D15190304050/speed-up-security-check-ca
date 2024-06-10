package united.cn.suscc.dao;

import org.apache.ibatis.annotations.Mapper;
import united.cn.suscc.domain.entities.Questionnaire;

@Mapper
public interface QuestionnaireMapper
{
    int insert(Questionnaire questionnaire);
    long getAverageWaitingDays();
}
