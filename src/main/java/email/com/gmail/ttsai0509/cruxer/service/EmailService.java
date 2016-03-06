package email.com.gmail.ttsai0509.cruxer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired private JavaMailSenderImpl javaMailSender;
    @Value("${spring.mail.username}") private String from;
    @Value("${spring.mail.personal}") private String personal;

    @Async
    public void sendEmail(String to, String subject, String text,
                          boolean isMultipart, boolean isHtml) {

        log.debug(
                "Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, text
        );

        try {
            MimeMessage mm = javaMailSender.createMimeMessage();
            MimeMessageHelper mmh = new MimeMessageHelper(mm, isMultipart, "UTF-8");
            mmh.setFrom(from, personal);
            mmh.setTo(to);
            mmh.setSubject(subject);
            mmh.setText(text, isHtml);
            javaMailSender.send(mm);
            log.debug("Sent e-mail to User '{}'", to);

        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

}
