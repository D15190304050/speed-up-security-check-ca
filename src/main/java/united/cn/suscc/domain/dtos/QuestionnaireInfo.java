package united.cn.suscc.domain.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class QuestionnaireInfo
{
    private String email;
    private String visaType;
    private String applicationLocation;
    private String currentResidence;
    private String currentPassportCountry;
    private String gender;
    private Date applicationStartDate;
    private Date applicationEndDate;
    private boolean separatedWithFamily;
    private long separationWithFamilyInDays;
    private boolean delayedEnrollment;
    private boolean unableToChangeJob;
    private long missedAnniversariesCount;
    private long noNewsFromIrccInDays;
}
