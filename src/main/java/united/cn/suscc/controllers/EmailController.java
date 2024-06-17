package united.cn.suscc.controllers;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import united.cn.suscc.commons.ServiceResponse;
import united.cn.suscc.services.GmailService;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/email")
public class EmailController
{
    @Autowired
    private GmailService gmailService;

    @GetMapping("/try")
    public ServiceResponse<Boolean> trySend() throws TemplateException, MessagingException, IOException
    {
        log.info("Start to send...");
        gmailService.sendVerificationEmail("15190304050@163.com");
        return ServiceResponse.buildSuccessResponse(true);
    }
}
