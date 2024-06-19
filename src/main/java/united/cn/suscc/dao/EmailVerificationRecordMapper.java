package united.cn.suscc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import united.cn.suscc.domain.entities.EmailVerificationRecord;

import java.util.Date;

@Mapper
public interface EmailVerificationRecordMapper
{
    long insert(EmailVerificationRecord emailVerificationRecord);
    int getStateTimeByEmailAndCode(@Param("emailAddress") String emailAddress, @Param("code") String code);
    long updateVerificationState(@Param("emailAddress") String emailAddress, @Param("code") String code, @Param("state") int state);
}
