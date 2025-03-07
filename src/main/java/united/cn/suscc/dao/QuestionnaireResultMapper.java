package united.cn.suscc.dao;

import org.apache.ibatis.annotations.Mapper;
import united.cn.suscc.domain.dtos.AverageWaitingDaysByApplicationType;
import united.cn.suscc.domain.entities.QuestionnaireResult;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface QuestionnaireResultMapper
{
    int insert(QuestionnaireResult questionnaireResult);
    long countByEmail(String emailAddress);
    List<AverageWaitingDaysByApplicationType> getAverageWaitingDaysByApplicationType();
    BigDecimal getAverageWaitingDays();
}
