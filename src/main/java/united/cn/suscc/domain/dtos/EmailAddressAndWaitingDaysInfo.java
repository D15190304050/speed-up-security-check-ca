package united.cn.suscc.domain.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class EmailAddressAndWaitingDaysInfo
{
    private String emailAddress;
    private long waitingDays;
    private Date applicationSubmissionDate;
}
