package com.example.email.service;

import com.example.common_utils.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SendEmailService {
    @Autowired
    JavaMailSenderImpl mailSender;
    @Autowired
    RedisCache redisCache;

    @Async("taskExecutor")
    public void sendEmail(String emailAddress) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
                StringBuilder codeNum = new StringBuilder();
                int[] code = new int[3];
                Random random = new Random();
                //自动生成验证码
                for (int i = 0; i < 6; i++) {
                    int num = random.nextInt(10) + 48;
                    int uppercase = random.nextInt(26) + 65;
                    int lowercase = random.nextInt(26) + 97;
                    code[0] = num;
                    code[1] = uppercase;
                    code[2] = lowercase;
                    codeNum.append((char) code[random.nextInt(3)]);
                }
                System.out.println(codeNum);
                redisCache.setCacheObject(emailAddress,codeNum,5, TimeUnit.MINUTES);
                //标题
                helper.setSubject("您的验证码为：" + codeNum);
                //内容
                helper.setText("您好！感谢支持dsg的小站。您的验证码为：" + "<h2>" + codeNum + "</h2>" + "千万不能告诉别人哦！", true);
                //邮件接收者
                helper.setTo(emailAddress);
                //邮件发送者，必须和配置文件里的一样，不然授权码匹配不上
                helper.setFrom("pys.dsg@qq.com");
                mailSender.send(mimeMessage);
                System.out.println("邮件发送成功！"+emailAddress);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
