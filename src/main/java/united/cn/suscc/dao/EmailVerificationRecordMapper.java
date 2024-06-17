package united.cn.suscc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import united.cn.suscc.domain.entities.EmailVerificationRecord;

import java.util.Date;

@Mapper
public interface EmailVerificationRecordMapper
{
    long insert(EmailVerificationRecord emailVerificationRecord);
    Date getExpirationTimeByEmailAndCode(@Param("emailAddress") String emailAddress, @Param("code") String code);
}
