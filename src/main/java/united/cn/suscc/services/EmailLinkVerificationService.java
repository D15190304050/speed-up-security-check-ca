package united.cn.suscc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import united.cn.suscc.commons.ServiceResponse;
import united.cn.suscc.dao.EmailVerificationRecordMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
public class EmailLinkVerificationService
{
    public static final String VERIFICATION_PARAM = "code";
    public static final char EMAIL_CODE_SEPARATOR = '$';
    public static final int VERIFIED = 1;

    @Autowired
    private EmailVerificationRecordMapper emailVerificationRecordMapper;

    public ServiceResponse<Boolean> verifyCode(String code)
    {
        byte[] decode = Base64.getDecoder().decode(code);
        String emailAddressWithUuid = new String(decode, StandardCharsets.UTF_8);
        int indexOfQuestionMark = emailAddressWithUuid.lastIndexOf(EMAIL_CODE_SEPARATOR);
        if (indexOfQuestionMark == -1)
            return ServiceResponse.buildSuccessResponse(false, "Unacceptable verification code.");

        String emailAddress = emailAddressWithUuid.substring(0, indexOfQuestionMark);

        int verificationState = emailVerificationRecordMapper.getStateTimeByEmailAndCode(emailAddress, code);
        if (verificationState == VERIFIED)
            return ServiceResponse.buildSuccessResponse(true);

        long updatingCount = emailVerificationRecordMapper.updateVerificationState(emailAddress, code, 1);

        if (updatingCount == 1)
            return ServiceResponse.buildSuccessResponse(true);
        else
            return ServiceResponse.buildErrorResponse(-1, "Error updating verification state.");

        // update.

        // Verification of expiration time will be added in the future.
//        Date expirationTime = emailVerificationRecordMapper.getExpirationTimeByEmailAndCode(emailAddress, code);
//
//        if (expirationTime == null)
//            return ServiceResponse.buildSuccessResponse(false, "No record found for email address: " + emailAddress);
//
//        if (expirationTime.after(new Date()))
//            return ServiceResponse.buildSuccessResponse(false, "Expired record.");
    }
}
