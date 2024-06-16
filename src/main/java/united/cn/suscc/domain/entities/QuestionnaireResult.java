package united.cn.suscc.domain.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class QuestionnaireResult
{
    /**
     * ID of the questionnaire result.
     */
    private long id;

    /**
     * Email of the user.
     */
    private String emailAddress;

    /**
     * Application type.
     */
    private int applicationType;

    /**
     * Location where the user submits the application.
     */
    private int applicationSubmissionLocation;

    /**
     * Text of the location if not listed (select "Other").
     */
    private String applicationSubmissionLocationIfOther;

    /**
     * Current country of residence of the user.
     */
    private int countryOfResidence;

    /**
     * Current country of residence if not listed (select "Other").
     */
    private String countryOfResidenceIfOther;

    /**
     * Current country of the passport.
     */
    private int currentPassportCountry;

    /**
     * Current country of the passport if not listed (select "Other").
     */
    private String currentPassportCountryIfOther;

    /**
     * Gender of the user.
     */
    private int gender;

    /**
     * Gender of the user if not listed (select "Other").
     */
    private String genderIfOther;

    /**
     * Application submission date.
     */
    private Date applicationSubmissionDate;

    /**
     * Application end date.
     */
    private Date applicationEndDate;

    /**
     * Date of entering security screening.
     */
    private Date dateOfEnteringSecurityScreening;

    /**
     * Date of clearing security screening.
     */
    private Date dateOfClearingSecurityScreening;

    /**
     * Current state of application.
     */
    private int currentApplicationState;

    /**
     * Is the applicant currently separated from family?
     */
    private boolean separatedWithFamily;

    /**
     * Family members separated from the applicant.
     */
    private Integer separateFamilyMembers;

    /**
     * How long is the applicant separated from family, in months.
     */
    private Long separationWithFamilyInMonths;

    /**
     * Is the enrollment delayed?
     */
    private boolean delayedEnrollment;

    /**
     * The original school start date.
     */
    private Date originalSchoolStartDate;

    /**
     * The original graduation date.
     */
    private Date originalSchoolEndDate;

    /**
     * Is the applicant unable to change the job?
     */
    private boolean unableToChangeJob;

    /**
     * Current annual salary (in Canadian dollars).
     */
    private BigDecimal currentSalaryInCad;

    /**
     * Estimated annual salary (in Canadian dollars) of the new job.
     */
    private BigDecimal estimatedSalaryInCad;

    /**
     * The last update date from IRCC.
     */
    private Date lastUpdateDateFromIrcc;
}
