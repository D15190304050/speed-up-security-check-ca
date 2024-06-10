package united.cn.suscc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/hello")
public class HelloController
{
    @GetMapping("/sayHello")
    public String sayHello()
    {
        log.info("Enter sayHello()...");
        return "Hello World!";
    }
}
