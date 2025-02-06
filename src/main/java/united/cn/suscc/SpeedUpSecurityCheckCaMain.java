package united.cn.suscc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpeedUpSecurityCheckCaMain
{
    public static void main(String[] args)
    {
        SpringApplication.run(SpeedUpSecurityCheckCaMain.class);
    }
}
