package united.cn.suscc.domain.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WaitingDaysResponse
{
    private List<AverageWaitingDaysByApplicationType> averageWaitingDaysOfApplicationTypes;
    private BigDecimal averageWaitingDays;
}
