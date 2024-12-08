package dev_final_team10.GoodBuyUS.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    //메일 보내는 메소드
    public void sendEmailWithInlineImage(String to, String subject, String htmlContent, String imagePath) throws MessagingException, IOException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);


        // 이메일 기본 설정
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);


        // 이미지 첨부
        Resource resource = new ClassPathResource(imagePath);
        if (!resource.exists()) {
            throw new IOException("Image not found at path: " + imagePath);
        }
        helper.addInline("logo", resource);

        mailSender.send(mimeMessage);
    }
}
