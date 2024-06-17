package united.cn.suscc.domain.entities;

import lombok.Data;

import java.util.Date;

/**
 * Table for storing email verification records.
 */
@Data
public class EmailVerificationRecord
{
    /**
     * ID of the email verification record.
     */
    private long id;

    /**
     * Email address of the questionnaire.
     */
    private String emailAddress;

    /**
     * Verification code.
     */
    private String code;

    /**
     * Creation time of the record.
     */
    private Date creationTime;

    /**
     * Expiration time of the code, by default, we set it to creationTime + 10 days.
     */
    private Date expirationTime;

    /**
     * Update time of the record, which is when the user clicks the link.
     */
    private Date updateTime;

    /**
     * Current state of the record: 0 - Submitted; 1 - Confirmed; 2 - Expired.
     */
    private int state;
}
