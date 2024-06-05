package apiwork.utils;

import io.lettuce.core.codec.StringCodec;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MailUtil {

    @Autowired
    private JavaMailSender mailSender;

    public String sendVerificationCode(String toAddress, String verificationCode) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toAddress);
        message.setSubject("Verification Code");
        message.setText("Your verification code is: " + verificationCode);
        message.setFrom("3314266280@qq.com");
        System.out.println(message);
        mailSender.send(message);
        return verificationCode;
    }

//    private String generateRandomCode(int length) {
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            sb.append(random.nextInt(10));
//        }
//        return sb.toString();
//    }

}
