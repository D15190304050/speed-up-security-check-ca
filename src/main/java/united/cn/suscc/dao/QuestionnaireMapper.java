package united.cn.suscc.dao;

import org.apache.ibatis.annotations.Mapper;
import united.cn.suscc.domain.entities.QuestionnaireResult;

@Mapper
public interface QuestionnaireMapper
{
    int insert(QuestionnaireResult questionnaireResult);
    long countByEmail(String emailAddress);
    long getAverageWaitingDays();
}
