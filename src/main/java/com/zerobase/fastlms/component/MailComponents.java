package com.zerobase.fastlms.component;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class MailComponents {
    private final JavaMailSender javaMailSender;

    public void sendMailTest() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("sangwoo98.kang@gmail.com");
        message.setSubject("안녕하세요. 제로베이스 입니다.");
        message.setText(" 안녕하세요. 제로베이스 입니다. 반갑습니다. ");
        javaMailSender.send(message);
    }

    public boolean sendMail(String mail, String subject, String text) {
        MimeMessagePreparator message = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper = new MimeMessageHelper(
                        mimeMessage,
                        true,
                        "UTF-8");
                helper.setTo(mail);
                helper.setSubject(subject);
                helper.setText(text, true);
            }
        };

        try {
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
