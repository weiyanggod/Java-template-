package com.example.app.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@RestController
@RequestMapping("/BFBidding")
public class Common {

    // 获取验证类
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(120, 40, 4, 0);

        // 获取验证码中的文字内容
        request.getSession().setAttribute("captcha", circleCaptcha.getCode());
        // 图形验证码写出，可以写出到文件，也可以写出到流
        circleCaptcha.write(response.getOutputStream());
    }

}
