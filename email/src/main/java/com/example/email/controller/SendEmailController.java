package com.example.email.controller;

import com.example.common_utils.entity.Errors;
import com.example.common_utils.entity.R;
import com.example.common_utils.utils.RedisCache;
import com.example.email.entity.EmailDto;
import com.example.email.service.SendEmailService;
import com.sun.jmx.snmp.tasks.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class SendEmailController {
    @Autowired
    SendEmailService sendEmailService;
    @Autowired
    RedisCache redisCache;

    @PostMapping("send")
    public R sendEmail(@RequestBody EmailDto emailDto) {
        String emailAddress = emailDto.getEmailAddress();
        System.out.println(emailAddress);
        String check = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(emailAddress);
        boolean isMatched = matcher.matches();
        if (isMatched) {
            if (redisCache.getCacheObject(emailAddress)!=null){
                return R.fail(Errors.FrequentOperationsError);
            }
                sendEmailService.sendEmail(emailAddress);
            return R.success(null, "success");
        } else {
            return R.fail(Errors.RegisterEmailError);
        }
    }
}
