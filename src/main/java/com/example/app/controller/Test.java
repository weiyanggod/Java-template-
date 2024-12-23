package com.example.app.controller;

import com.example.app.config.Result;
import com.example.app.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class Test {
    @Autowired
    PersonService personService;

    /**
     * 测试接口
     *
     * @return
     */
    @GetMapping("/test")
    public Result<String> textApi() {
        log.info("测试日志");
        personService.getList();
        return Result.success("成功");
    }
}
