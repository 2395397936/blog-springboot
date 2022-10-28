import com.example.email.EmailApp;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@SpringBootTest(classes = EmailApp.class)
@RunWith(SpringRunner.class)
public class Test {
    @Autowired
    JavaMailSenderImpl mailSender;

    @org.junit.Test
    public void contextLoads() throws MessagingException{
        int count=1;//默认发送一次
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        while(count--!=0){
            String codeNum = "";
            int [] code = new int[3];
            Random random = new Random();
            //自动生成验证码
            for (int i = 0; i < 6; i++) {
                int num = random.nextInt(10) + 48;
                int uppercase = random.nextInt(26) + 65;
                int lowercase = random.nextInt(26) + 97;
                code[0] = num;
                code[1] = uppercase;
                code[2] = lowercase;
                codeNum+=(char)code[random.nextInt(3)];
            }
            System.out.println(codeNum);
            //标题
            helper.setSubject("您的验证码为："+codeNum);
            //内容
            helper.setText("您好！，感谢支持冰咖啡的小站。您的验证码为："+"<h2>"+codeNum+"</h2>"+"千万不能告诉别人哦！",true);
            //邮件接收者
            helper.setTo("2395397936@qq.com");
            //邮件发送者，必须和配置文件里的一样，不然授权码匹配不上
            helper.setFrom("pys.dsg@qq.com");
            mailSender.send(mimeMessage);
            System.out.println("邮件发送成功！"+(count+1));
        }
    }
}
