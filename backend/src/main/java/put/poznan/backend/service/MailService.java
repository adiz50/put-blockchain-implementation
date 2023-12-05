package put.poznan.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import put.poznan.backend.entities.Mail;
import put.poznan.backend.exception.InvalidEmail;
import put.poznan.backend.repository.MailRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailRepository mailRepository;
    private final JavaMailSender sender;
    @Value("${spring.mail.username}")
    private String senderAddress;

    public void addMail( Mail mail ) {
        if ( ! EmailValidator.getInstance().isValid( mail.getEmail() ) ) {
            throw new InvalidEmail( "Invalid email" );
        }
        mailRepository.save( mail );
    }

    public void deleteMail( Mail mail ) {
        mailRepository.delete( mail );
    }

    public void deleteMail( UUID uuid ) {
        mailRepository.deleteById( uuid );
    }

    @Scheduled(fixedDelay = 5000)
    private void sendMails() {
        List<Mail> mails = mailRepository.getMailsToSend();
        for ( Mail mail : mails ) {
            MimeMessage message = sender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper( message,
                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        StandardCharsets.UTF_8.name() );
                helper.setText( mail.getBody(), false );
                helper.setSubject( mail.getSubject() );
                helper.setFrom( senderAddress );
                helper.setTo( mail.getEmail() );
                sender.send( message );
            } catch ( MessagingException e ) {
                deleteMail( mail );
            } finally {
                deleteMail( mail );
            }
        }
    }
}
