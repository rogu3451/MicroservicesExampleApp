package pl.karolrogozinski.notifications.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.karolrogozinski.notifications.dto.EmailDto;

import javax.mail.MessagingException;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    EmailSender emailSender;

    @Test
    public void send_email_test() throws MessagingException {

        for(int i=0; i<100; i++){
            EmailDto emailDto = EmailDto.builder()
                    .to("rogu3451@interia.eu")
                    .title("Hejo!!!")
                    .content("Test1!!!")
                    .build();
            emailSender.sendEmail(emailDto);
        }

    }

}
