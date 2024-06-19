package united.cn.suscc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import united.cn.suscc.commons.ServiceResponse;
import united.cn.suscc.services.EmailLinkVerificationService;

@Slf4j
@RestController
@RequestMapping("/verification")
public class VerificationController
{
    @Autowired
    private EmailLinkVerificationService emailLinkVerificationService;

    @GetMapping("/code")
    public ServiceResponse<Boolean> verifyCode(@RequestParam("code") String code)
    {
        return emailLinkVerificationService.verifyCode(code);
    }
}
