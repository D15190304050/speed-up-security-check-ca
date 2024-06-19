package united.cn.suscc.domain.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AverageWaitingDaysByApplicationType
{
    private int applicationType;
    private BigDecimal averageWaitingDays;
}
