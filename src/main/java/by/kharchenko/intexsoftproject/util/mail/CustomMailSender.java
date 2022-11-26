package by.kharchenko.intexsoftproject.util.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class CustomMailSender {

    @Value("${spring.mail.username}")
    private String fromAddress;

    private final MailSender mailSender;

    @Autowired
    public CustomMailSender(@Qualifier(value = "mailSender") MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendCustomEmail(String toAddress, String subject, String msgBody) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(msgBody);
        mailSender.send(simpleMailMessage);
    }
}
