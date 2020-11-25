package pl.karolrogozinski.notifications.service;

import pl.karolrogozinski.notifications.dto.EmailDto;
import pl.karolrogozinski.notifications.dto.NotificationInfoDto;

import javax.mail.MessagingException;

public interface EmailSender {

    void sendEmails(NotificationInfoDto notificationInfo);

    void sendEmail(EmailDto emailDto) throws MessagingException;

}
